package technologyforall.com.chargeservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import technologyforall.com.chargeservice.event.ChargeCalculatedEvent;
import technologyforall.com.chargeservice.event.TransferInitiatedEvent;
import technologyforall.com.chargeservice.model.Charge;
import technologyforall.com.chargeservice.model.ChargeTier;
import technologyforall.com.chargeservice.repository.ChargeRepository;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferInitiatedConsumer {

    private final ChargeRepository chargeRepository;
    private final ChargeCalculatedProducer chargeCalculatedProducer;

    @KafkaListener(topics = "transfer.initiated", groupId = "charge-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleTransferInitiated(TransferInitiatedEvent event) {
        log.info("Received TransferInitiatedEvent for transferId={}", event.getTransferId());

        // Idempotency: skip if already processed this transfer
        boolean alreadyProcessed = chargeRepository.existsByTransferId(event.getTransferId());
        if (alreadyProcessed) {
            log.warn("Duplicate event detected for transferId={} — skipping", event.getTransferId());
            return;
        }

        BigDecimal amount = event.getTransferAmount();
        ChargeTier tier = ChargeTier.forAmount(amount);
        BigDecimal fee = tier.calculateFee(amount);
        BigDecimal netAmount = amount.subtract(fee);

        Charge charge = new Charge();
        charge.setTransferId(event.getTransferId());
        charge.setTransferAmount(amount);
        charge.setCharge(fee);
        charge.setSourceAcct(event.getSourceAccountNumber());
        charge.setDestAcct(event.getVirtualAccountNumber());
        charge.setSourceBankName(event.getSourceBankName());
        chargeRepository.save(charge);

        ChargeCalculatedEvent outEvent = new ChargeCalculatedEvent(
                event.getTransferId(), event.getReferenceNumber(), event.getBusinessId(),
                event.getVirtualAccountNumber(), event.getSourceAccountNumber(),
                event.getSourceBankName(), amount, fee, tier.getRateLabel(), netAmount);
        chargeCalculatedProducer.publishChargeCalculated(outEvent);

        log.info("Charge calculated for transferId={} fee={} net={}", event.getTransferId(), fee, netAmount);
    }
}
