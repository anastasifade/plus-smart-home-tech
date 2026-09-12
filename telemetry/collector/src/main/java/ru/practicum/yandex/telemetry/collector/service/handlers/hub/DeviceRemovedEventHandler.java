package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;
import ru.practicum.yandex.telemetry.collector.model.hub.device.DeviceRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public final class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    @Override
    protected DeviceRemovedEventAvro toAvro(HubEvent event) {
        return HubEventMapper.toAvro((DeviceRemovedEvent) event);
    }

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
