package ru.yandex.practicum.telemetry.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaAggregatorTopics {
    private String snapshots;
    private String sensors;
}
