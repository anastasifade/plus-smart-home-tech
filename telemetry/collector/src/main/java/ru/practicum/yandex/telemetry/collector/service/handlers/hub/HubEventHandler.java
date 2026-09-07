package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;

public interface HubEventHandler {
    HubEventType getType();

    void handle(HubEvent event);
}
