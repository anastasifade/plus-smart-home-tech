package ru.practicum.yandex.telemetry.collector.constants;

public final class KafkaTopics {
    private KafkaTopics() {
    }

    public static final String SENSOR_EVENTS_TOPIC = "telemetry.sensors.v1";
    public static final String HUB_EVENTS_TOPIC = "telemetry.hubs.v1";
}
