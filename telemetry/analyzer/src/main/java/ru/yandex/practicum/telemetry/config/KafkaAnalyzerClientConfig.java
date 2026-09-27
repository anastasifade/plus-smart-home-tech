package ru.yandex.practicum.telemetry.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaAnalyzerClientConfig {
    private final KafkaAnalyzerConfig config;

    @Bean
    public Consumer<String, SpecificRecordBase> getHubsConsumer() {
        return new KafkaConsumer<>(config.getHubsConsumerProperties());
    }

    @Bean
    public Consumer<String, SpecificRecordBase> getSnapshotsConsumer() {
        return new KafkaConsumer<>(config.getSnapshotsConsumerProperties());
    }
}
