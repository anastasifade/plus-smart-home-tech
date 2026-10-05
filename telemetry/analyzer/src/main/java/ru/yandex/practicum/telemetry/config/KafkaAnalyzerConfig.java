package ru.yandex.practicum.telemetry.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaAnalyzerConfig {
    private String bootstrapServers;
    private KafkaAnalyzerTopics topics;
    private KafkaAnalyzerConsumerConfig consumer;
}
