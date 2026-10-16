package ru.practicum.yandex.telemetry.collector.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaCollectorConfig {
    private String bootstrapServers;
    private Properties properties;
    private KafkaCollectorTopics topics;
}
