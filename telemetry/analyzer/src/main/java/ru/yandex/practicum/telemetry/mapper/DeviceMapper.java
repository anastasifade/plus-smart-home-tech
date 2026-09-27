package ru.yandex.practicum.telemetry.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.telemetry.model.Sensor;

@UtilityClass
public class DeviceMapper {

    public Sensor toSensor(String hubId, String deviceId) {
        Sensor sensor = new Sensor();
        sensor.setHubId(hubId);
        sensor.setId(deviceId);
        return sensor;
    }
}
