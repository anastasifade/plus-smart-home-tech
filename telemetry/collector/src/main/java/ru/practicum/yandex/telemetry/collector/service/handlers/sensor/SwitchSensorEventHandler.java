package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;
import ru.practicum.yandex.telemetry.collector.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public final class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro>{
    @Override
    protected SwitchSensorAvro toAvro(SensorEvent event) {
        return SensorEventMapper.toAvro((SwitchSensorEvent) event);
    }

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
