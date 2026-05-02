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
}
