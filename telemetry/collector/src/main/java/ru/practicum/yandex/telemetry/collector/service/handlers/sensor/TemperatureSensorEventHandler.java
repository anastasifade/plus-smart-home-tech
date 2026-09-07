package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;
import ru.practicum.yandex.telemetry.collector.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public final class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {
    @Override
    protected TemperatureSensorAvro toAvro(SensorEvent event) {
        return SensorEventMapper.toAvro((TemperatureSensorEvent) event);
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
