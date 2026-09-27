package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import org.springframework.stereotype.Component;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;

@Component
public final class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    @Override
    protected DeviceAddedEventAvro toAvro(HubEventProto event) {
        return HubEventMapper.toAvro(event.getDeviceAdded());
    }

    @Override
    public HubEventProto.PayloadCase getType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}
