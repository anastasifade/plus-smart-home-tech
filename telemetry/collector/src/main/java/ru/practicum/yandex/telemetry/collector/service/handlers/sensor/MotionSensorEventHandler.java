package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.practicum.yandex.telemetry.collector.model.sensor.MotionSensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public final class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {
    @Override
    protected MotionSensorAvro toAvro(SensorEvent event) {
        return SensorEventMapper.toAvro((MotionSensorEvent) event);
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
