package ru.practicum.yandex.telemetry.collector.model.sensor;

import lombok.ToString;

@ToString(callSuper = true)
public class UnknownSensorEvent extends SensorEvent{
    @Override
    public SensorEventType getType() {
        return SensorEventType.UNKNOWN_EVENT;
    }
}
