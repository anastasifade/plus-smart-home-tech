package ru.practicum.yandex.telemetry.collector.model.hub.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    /**
     * Added device (sensor) id.
     */
    @NotBlank(message = "Device id cannot be null or blank.")
    private String id;

    /**
     * Type of added device.
     */
    @NotNull(message = "Device type cannot be null.")
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
