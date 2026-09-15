package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public final class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    @Override
    protected ClimateSensorAvro toAvro(SensorEventProto event) {
        return SensorEventMapper.toAvro(event.getClimateSensorEvent());
    }

    @Override
    public SensorEventProto.PayloadCase getType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }
}
