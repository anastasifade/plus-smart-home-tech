package ru.practicum.yandex.telemetry.collector.model.hub;

import lombok.ToString;

@ToString(callSuper = true)
public class UnknownHubEvent extends HubEvent {
    @Override
    public HubEventType getType() {
        return HubEventType.UNKNOWN;
    }
}
