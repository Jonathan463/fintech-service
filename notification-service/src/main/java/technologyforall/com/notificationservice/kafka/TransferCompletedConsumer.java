package technologyforall.com.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import technologyforall.com.notificationservice.event.TransferCompletedEvent;

@Slf4j
@Component
public class TransferCompletedConsumer {

    @KafkaListener(topics = "transfer.completed", groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleTransferCompleted(TransferCompletedEvent event) {
        log.info("=== NOTIFICATION ===");
        log.info("Transfer COMPLETED — ref={} businessId={} amount={} charge={} net={} at={}",
                event.getReferenceNumber(),
                event.getBusinessId(),
                event.getTransferAmount(),
                event.getChargeAmount(),
                event.getNetAmount(),
                event.getCompletedAt());
        // Future: send email / SMS / push notification to business owner
    }
}
