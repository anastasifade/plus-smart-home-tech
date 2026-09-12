package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;
import ru.practicum.yandex.telemetry.collector.model.hub.device.DeviceAddedEvent;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;

@Component
public final class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    @Override
    protected DeviceAddedEventAvro toAvro(HubEvent event) {
        return HubEventMapper.toAvro((DeviceAddedEvent) event);
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
