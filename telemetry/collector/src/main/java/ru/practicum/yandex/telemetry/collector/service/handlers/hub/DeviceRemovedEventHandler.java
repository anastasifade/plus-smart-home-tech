package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public final class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    @Override
    protected DeviceRemovedEventAvro toAvro(HubEventProto event) {
        return HubEventMapper.toAvro(event.getDeviceRemoved());
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }
}
