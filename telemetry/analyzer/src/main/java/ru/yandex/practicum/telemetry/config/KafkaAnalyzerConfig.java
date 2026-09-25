package ru.yandex.practicum.telemetry.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaAnalyzerConfig {
    private String bootstrapServers;
    private KafkaAnalyzerTopics topics;
    private KafkaAnalyzerConsumerConfig consumer;

    public Properties getHubsConsumerProperties() {
        Properties properties = getCommonConsumerProperties();
        properties.putAll(consumer.getHubs());
        return properties;
    }

    public Properties getSnapshotsConsumerProperties() {
        Properties properties = getCommonConsumerProperties();
        properties.putAll(consumer.getSnapshots());
        return properties;
    }

    private Properties getCommonConsumerProperties() {
        Properties properties = new Properties();
        properties.putAll(consumer.getCommon());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return properties;
    }

    @Bean
    public KafkaAnalyzerTopics getTopics() {
        return topics;
    }
}
