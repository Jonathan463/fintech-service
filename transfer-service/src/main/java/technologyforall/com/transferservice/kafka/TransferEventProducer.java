package technologyforall.com.transferservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import technologyforall.com.transferservice.event.TransferCompletedEvent;
import technologyforall.com.transferservice.event.TransferInitiatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishTransferInitiated(TransferInitiatedEvent event) {
        String key = String.valueOf(event.getBusinessId());
        kafkaTemplate.send("transfer.initiated", key, event);
        log.info("Published TransferInitiatedEvent for transferId={} businessId={}",
                event.getTransferId(), event.getBusinessId());
    }

    public void publishTransferCompleted(TransferCompletedEvent event) {
        String key = String.valueOf(event.getBusinessId());
        kafkaTemplate.send("transfer.completed", key, event);
        log.info("Published TransferCompletedEvent for transferId={} status={}",
                event.getTransferId(), event.getStatus());
    }
}
