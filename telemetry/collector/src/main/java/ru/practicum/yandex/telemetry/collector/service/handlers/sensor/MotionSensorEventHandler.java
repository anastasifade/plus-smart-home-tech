package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public final class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {
    @Override
    protected MotionSensorAvro toAvro(SensorEventProto event) {
        return SensorEventMapper.toAvro(event.getMotionSensorEvent());
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }
}
