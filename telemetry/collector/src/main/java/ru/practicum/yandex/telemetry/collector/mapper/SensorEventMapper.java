package ru.practicum.yandex.telemetry.collector.mapper;

import lombok.experimental.UtilityClass;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@UtilityClass
public class SensorEventMapper {
    public <T extends SpecificRecordBase> SensorEventAvro toAvro(SensorEventProto event, T payload) {
        return SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();
    }

    public ClimateSensorAvro toAvro(ClimateSensorEventProto event) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setHumidity(event.getHumidity())
                .setCo2Level(event.getCo2Level())
                .build();
    }

    public LightSensorAvro toAvro(LightSensorEventProto event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    public MotionSensorAvro toAvro(MotionSensorEventProto event) {
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(event.getLinkQuality())
                .setMotion(event.getMotion())
                .setVoltage(event.getVoltage())
                .build();
    }

    public SwitchSensorAvro toAvro(SwitchSensorEventProto event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getState())
                .build();
    }

    public TemperatureSensorAvro toAvro(TemperatureSensorEventProto event) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}
