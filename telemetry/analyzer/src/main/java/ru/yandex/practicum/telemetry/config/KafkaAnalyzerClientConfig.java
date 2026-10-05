package ru.yandex.practicum.telemetry.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaAnalyzerClientConfig {
    private final KafkaAnalyzerConfig config;

    @Bean
    public Consumer<String, SpecificRecordBase> getHubsConsumer() {
        Properties properties = getCommonConsumerProperties();
        properties.putAll(config.getConsumer().getHubs());
        return new KafkaConsumer<>(properties);
    }

    @Bean
    public Consumer<String, SpecificRecordBase> getSnapshotsConsumer() {
        Properties properties = getCommonConsumerProperties();
        properties.putAll(config.getConsumer().getSnapshots());
        return new KafkaConsumer<>(properties);
    }

    @Bean
    public KafkaAnalyzerTopics getTopics() {
        return config.getTopics();
    }

    private Properties getCommonConsumerProperties() {
        Properties properties = new Properties();
        properties.putAll(config.getConsumer().getCommon());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        return properties;
    }
}
