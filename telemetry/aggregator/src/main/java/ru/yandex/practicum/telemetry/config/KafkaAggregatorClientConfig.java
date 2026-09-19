package ru.yandex.practicum.telemetry.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.telemetry.messaging.KafkaSnapshotProducer;

@Configuration
@RequiredArgsConstructor
public class KafkaAggregatorClientConfig {

    private final KafkaAggregatorConfig config;

    @Bean
    public Consumer<String, SpecificRecordBase> getConsumer() {
        return new KafkaConsumer<>(config.getConsumerProperties());
    }

    @Bean(destroyMethod = "close")
    public Producer<String, SpecificRecordBase> getProducer() {
        return new KafkaProducer<>(config.getProducerProperties());
    }

    @Bean(destroyMethod = "close")
    public KafkaSnapshotProducer getSnapshotProducer(Producer<String, SpecificRecordBase> producer) {
        return new KafkaSnapshotProducer(producer, config.getTopics().getSnapshots());
    }
}
