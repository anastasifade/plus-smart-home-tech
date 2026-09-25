package ru.yandex.practicum.telemetry.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter(value = AccessLevel.PACKAGE)
@Setter
public class KafkaAnalyzerConsumerConfig {
    private Map<String, String> common;
    private Map<String, String> hubs;
    private Map<String, String> snapshots;
}
