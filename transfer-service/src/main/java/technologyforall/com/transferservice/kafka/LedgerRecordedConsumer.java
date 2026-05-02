package technologyforall.com.transferservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import technologyforall.com.transferservice.event.LedgerRecordedEvent;
import technologyforall.com.transferservice.event.TransferCompletedEvent;
import technologyforall.com.transferservice.model.Transfer;
import technologyforall.com.transferservice.model.TransferStatus;
import technologyforall.com.transferservice.repository.TransferRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class LedgerRecordedConsumer {

    private final TransferRepository transferRepository;
    private final TransferEventProducer transferEventProducer;

    @KafkaListener(topics = "ledger.recorded", groupId = "transfer-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleLedgerRecorded(LedgerRecordedEvent event) {
        log.info("Received LedgerRecordedEvent for transferId={}", event.getTransferId());

        Transfer transfer = transferRepository.findById(event.getTransferId()).orElse(null);
        if (transfer == null) {
            log.error("Transfer not found for id={} — cannot mark as COMPLETED", event.getTransferId());
            return;
        }

        if (TransferStatus.COMPLETED.equals(transfer.getStatus())) {
            log.warn("Transfer {} already COMPLETED — skipping duplicate event", event.getTransferId());
            return;
        }

        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setUpdatedAt(LocalDateTime.now());
        transferRepository.save(transfer);

        TransferCompletedEvent completed = new TransferCompletedEvent(
                transfer.getId(),
                transfer.getReferenceNumber(),
                transfer.getBusinessId(),
                transfer.getVirtualAccountNumber(),
                transfer.getTransferAmount(),
                transfer.getChargeAmount(),
                transfer.getNetAmount(),
                TransferStatus.COMPLETED.name(),
                LocalDateTime.now());
        transferEventProducer.publishTransferCompleted(completed);

        log.info("Transfer {} marked as COMPLETED and TransferCompletedEvent published", transfer.getId());
    }
}
