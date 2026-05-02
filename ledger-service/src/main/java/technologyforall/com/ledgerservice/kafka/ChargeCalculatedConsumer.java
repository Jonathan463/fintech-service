package technologyforall.com.ledgerservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import technologyforall.com.ledgerservice.event.ChargeCalculatedEvent;
import technologyforall.com.ledgerservice.event.LedgerRecordedEvent;
import technologyforall.com.ledgerservice.model.LedgerEntryType;
import technologyforall.com.ledgerservice.repository.LedgerEntryRepository;
import technologyforall.com.ledgerservice.service.LedgerService;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChargeCalculatedConsumer {

    private final LedgerService ledgerService;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerRecordedProducer ledgerRecordedProducer;

    @KafkaListener(topics = "charge.calculated", groupId = "ledger-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleChargeCalculated(ChargeCalculatedEvent event) {
        log.info("Received ChargeCalculatedEvent for transferId={}", event.getTransferId());

        // Idempotency: skip if CREDIT entry already exists for this transfer
        boolean alreadyProcessed = ledgerEntryRepository
                .existsByTransferIdAndEntryType(event.getTransferId(), LedgerEntryType.CREDIT);
        if (alreadyProcessed) {
            log.warn("Duplicate ChargeCalculatedEvent for transferId={} — skipping", event.getTransferId());
            return;
        }

        int entriesCreated = ledgerService.recordEntries(event);

        LedgerRecordedEvent outEvent = new LedgerRecordedEvent(
                event.getTransferId(),
                event.getReferenceNumber(),
                event.getBusinessId(),
                entriesCreated,
                LocalDateTime.now());
        ledgerRecordedProducer.publishLedgerRecorded(outEvent);
    }
}
