package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.practicum.yandex.telemetry.collector.model.sensor.ClimateSensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public final class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    @Override
    protected ClimateSensorAvro toAvro(SensorEvent event) {
        return SensorEventMapper.toAvro((ClimateSensorEvent) event);
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
