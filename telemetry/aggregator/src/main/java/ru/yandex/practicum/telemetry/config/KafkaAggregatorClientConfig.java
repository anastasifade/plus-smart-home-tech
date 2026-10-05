package ru.yandex.practicum.telemetry.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.telemetry.messaging.KafkaSnapshotProducer;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaAggregatorClientConfig {

    private final KafkaAggregatorConfig config;

    @Bean
    public Consumer<String, SpecificRecordBase> getConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.putAll(config.getConsumer());
        return new KafkaConsumer<>(properties);
    }

    @Bean(destroyMethod = "close")
    public Producer<String, SpecificRecordBase> getProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        properties.putAll(config.getProducer());
        return new KafkaProducer<>(properties);
    }

    @Bean(destroyMethod = "close")
    public KafkaSnapshotProducer getSnapshotProducer(Producer<String, SpecificRecordBase> producer) {
        return new KafkaSnapshotProducer(producer, config.getTopics().getSnapshots());
    }

    @Bean
    public KafkaAggregatorTopics getTopics() {
        return config.getTopics();
    }
}
