package ru.yandex.practicum.telemetry.mapper;

import com.google.protobuf.Timestamp;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.telemetry.model.Action;

import java.time.Instant;

@UtilityClass
public class ActionRequestMapper {

    public DeviceActionRequest toDeviceActionRequest(String hubId, String sensorId, String scenario, Action action) {
        Instant timestamp = Instant.now();
        return DeviceActionRequest.newBuilder()
                .setAction(DeviceActionProto.newBuilder()
                        .setSensorId(sensorId)
                        .setType(ActionTypeProto.valueOf(action.getType().toString()))
                        .setValue(action.getValue())
                        .build())
                .setHub1(hubId)
                .setScenarioName(scenario)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(timestamp.getEpochSecond())
                        .setNanos(timestamp.getNano())
                        .build())
                .build();
    }
}
