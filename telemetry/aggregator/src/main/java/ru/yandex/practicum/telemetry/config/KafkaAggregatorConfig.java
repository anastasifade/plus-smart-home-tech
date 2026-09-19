package ru.yandex.practicum.telemetry.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaAggregatorConfig {
    private String bootstrapServers;
    private Map<String, String> producer;
    private Map<String, String> consumer;
    private KafkaAggregatorTopics topics;

    public Properties getProducerProperties() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.putAll(producer);
        return config;
    }

    public Properties getConsumerProperties() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.putAll(consumer);
        return config;
    }

    @Bean
    public KafkaAggregatorTopics getTopics() {
        return topics;
    }
}
