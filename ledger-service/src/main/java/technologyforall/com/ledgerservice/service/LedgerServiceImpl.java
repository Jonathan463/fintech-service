package technologyforall.com.ledgerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import technologyforall.com.ledgerservice.client.ProfileServiceClient;
import technologyforall.com.ledgerservice.dto.PartnerDto;
import technologyforall.com.ledgerservice.event.ChargeCalculatedEvent;
import technologyforall.com.ledgerservice.model.LedgerEntry;
import technologyforall.com.ledgerservice.model.LedgerEntryType;
import technologyforall.com.ledgerservice.repository.LedgerEntryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LedgerServiceImpl implements LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final ProfileServiceClient profileServiceClient;

    @Override
    @Transactional
    public int recordEntries(ChargeCalculatedEvent event) {
        List<LedgerEntry> entries = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // 1. CREDIT — full transfer amount received into the virtual account
        LedgerEntry credit = new LedgerEntry();
        credit.setTransferId(event.getTransferId());
        credit.setReferenceNumber(event.getReferenceNumber());
        credit.setBusinessId(event.getBusinessId());
        credit.setEntryType(LedgerEntryType.CREDIT);
        credit.setAmount(event.getTransferAmount());
        credit.setDestinationAccount(event.getVirtualAccountNumber());
        credit.setDescription("Inbound transfer from " + event.getSourceAccountNumber()
                + " (" + event.getSourceBankName() + ")");
        credit.setCreatedAt(now);
        entries.add(credit);

        // 2. CHARGE_DEBIT — fee deducted
        LedgerEntry chargeDr = new LedgerEntry();
        chargeDr.setTransferId(event.getTransferId());
        chargeDr.setReferenceNumber(event.getReferenceNumber());
        chargeDr.setBusinessId(event.getBusinessId());
        chargeDr.setEntryType(LedgerEntryType.CHARGE_DEBIT);
        chargeDr.setAmount(event.getChargeAmount());
        chargeDr.setDescription("Service charge (" + event.getChargeRate() + ")");
        chargeDr.setCreatedAt(now);
        entries.add(chargeDr);

        // 3. PARTNER_PAYOUT — net amount split across partners
        List<PartnerDto> partners = profileServiceClient.getPartners(event.getBusinessId());
        BigDecimal netAmount = event.getNetAmount();

        for (PartnerDto partner : partners) {
            BigDecimal payout = netAmount
                    .multiply(BigDecimal.valueOf(partner.getRatio()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            LedgerEntry pOut = new LedgerEntry();
            pOut.setTransferId(event.getTransferId());
            pOut.setReferenceNumber(event.getReferenceNumber());
            pOut.setBusinessId(event.getBusinessId());
            pOut.setEntryType(LedgerEntryType.PARTNER_PAYOUT);
            pOut.setAmount(payout);
            pOut.setDestinationAccount(partner.getDestination_account());
            pOut.setDestinationBankName(partner.getDestination_bank_name());
            pOut.setDescription("Payout to " + partner.getName()
                    + " (" + partner.getRatio() + "%)");
            pOut.setCreatedAt(now);
            entries.add(pOut);
        }

        ledgerEntryRepository.saveAll(entries);
        log.info("Recorded {} ledger entries for transferId={}", entries.size(), event.getTransferId());
        return entries.size();
    }
}
