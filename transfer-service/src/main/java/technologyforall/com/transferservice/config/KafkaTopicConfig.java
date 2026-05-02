package technologyforall.com.transferservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic transferInitiatedTopic() {
        return TopicBuilder.name("transfer.initiated")
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic chargeCalculatedTopic() {
        return TopicBuilder.name("charge.calculated")
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic ledgerRecordedTopic() {
        return TopicBuilder.name("ledger.recorded")
                .partitions(3)
                .replicas(3)
                .build();
    }

    @Bean
    public NewTopic transferCompletedTopic() {
        return TopicBuilder.name("transfer.completed")
                .partitions(3)
                .replicas(3)
                .build();
    }

    // Dead Letter Topics — failed messages land here after 3 retries
    @Bean
    public NewTopic transferInitiatedDlt() {
        return TopicBuilder.name("transfer.initiated.DLT").partitions(3).replicas(3).build();
    }

    @Bean
    public NewTopic chargeCalculatedDlt() {
        return TopicBuilder.name("charge.calculated.DLT").partitions(3).replicas(3).build();
    }

    @Bean
    public NewTopic ledgerRecordedDlt() {
        return TopicBuilder.name("ledger.recorded.DLT").partitions(3).replicas(3).build();
    }

    @Bean
    public NewTopic transferCompletedDlt() {
        return TopicBuilder.name("transfer.completed.DLT").partitions(3).replicas(3).build();
    }
}
