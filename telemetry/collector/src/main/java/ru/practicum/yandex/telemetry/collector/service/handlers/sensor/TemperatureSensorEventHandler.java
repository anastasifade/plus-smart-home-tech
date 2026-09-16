package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public final class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {
    @Override
    protected TemperatureSensorAvro toAvro(SensorEventProto event) {
        return SensorEventMapper.toAvro(event.getTemperatureSensorEvent());
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
