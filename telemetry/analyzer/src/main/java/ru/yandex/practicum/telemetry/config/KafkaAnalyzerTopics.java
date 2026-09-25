package ru.yandex.practicum.telemetry.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaAnalyzerTopics {
    private String hubs;
    private String snapshots;
}
