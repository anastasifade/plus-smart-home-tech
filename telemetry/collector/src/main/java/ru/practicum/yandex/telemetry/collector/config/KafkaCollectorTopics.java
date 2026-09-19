package ru.practicum.yandex.telemetry.collector.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaCollectorTopics {
    private String sensors;
    private String hubs;
}
