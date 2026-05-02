package technologyforall.com.ledgerservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import technologyforall.com.ledgerservice.event.LedgerRecordedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class LedgerRecordedProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishLedgerRecorded(LedgerRecordedEvent event) {
        String key = String.valueOf(event.getBusinessId());
        kafkaTemplate.send("ledger.recorded", key, event);
        log.info("Published LedgerRecordedEvent for transferId={} entries={}",
                event.getTransferId(), event.getEntriesCreated());
    }
}
