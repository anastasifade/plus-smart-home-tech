package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.practicum.yandex.telemetry.collector.model.sensor.LightSensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public final class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    @Override
    protected LightSensorAvro toAvro(SensorEvent event) {
        return SensorEventMapper.toAvro((LightSensorEvent) event);
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
