package ru.yandex.practicum.telemetry.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaAggregatorConfig {
    private String bootstrapServers;
    private Map<String, String> producer;
    private Map<String, String> consumer;
    private KafkaAggregatorTopics topics;
}
