package technologyforall.com.chargeservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import technologyforall.com.chargeservice.event.ChargeCalculatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChargeCalculatedProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishChargeCalculated(ChargeCalculatedEvent event) {
        String key = String.valueOf(event.getBusinessId());
        kafkaTemplate.send("charge.calculated", key, event);
        log.info("Published ChargeCalculatedEvent for transferId={}", event.getTransferId());
    }
}
