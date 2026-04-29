package technologyforall.com.chargeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import technologyforall.com.chargeservice.dto.ChargeRequest;
import technologyforall.com.chargeservice.dto.ChargeResponse;
import technologyforall.com.chargeservice.exception.InvalidAmountException;
import technologyforall.com.chargeservice.model.Charge;
import technologyforall.com.chargeservice.model.ChargeTier;
import technologyforall.com.chargeservice.repository.ChargeRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ChargeServiceImpl implements ChargeService{

    private final ChargeRepository chargeRepository;

    public ChargeResponse calculateAndSaveCharge(ChargeRequest request) {
        BigDecimal amount = request.getTransferAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(
                    "Transfer amount must be greater than zero. Provided: " + amount);
        }

        ChargeTier tier = ChargeTier.forAmount(amount);
        BigDecimal fee = tier.calculateFee(amount);
        BigDecimal totalDebit = amount.add(fee);

        Charge charge = new Charge();
        charge.setTransferAmount(amount);
        charge.setCharge(fee);
        charge.setSourceAcct(request.getSourceAcct());
        charge.setDestAcct(request.getDestAcct());
        charge.setSourceBankName(request.getSourceBankName());
        Charge saved = chargeRepository.save(charge);

        return toResponse(saved, tier.getRateLabel(), totalDebit);
    }

    private ChargeResponse toResponse(Charge saved, String chargeRate, BigDecimal totalDebit) {
        ChargeResponse response = new ChargeResponse();
        response.setId(saved.getId());
        response.setSourceAcct(saved.getSourceAcct());
        response.setDestAcct(saved.getDestAcct());
        response.setSourceBankName(saved.getSourceBankName());
        response.setTransferAmount(saved.getTransferAmount());
        response.setChargeRate(chargeRate);
        response.setCharge(saved.getCharge());
        response.setTotalDebit(totalDebit);
        return response;
    }
}
