package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public final class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    @Override
    protected LightSensorAvro toAvro(SensorEventProto event) {
        return SensorEventMapper.toAvro(event.getLightSensorEvent());
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }
}
