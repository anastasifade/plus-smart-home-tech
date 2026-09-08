package ru.practicum.yandex.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.yandex.telemetry.collector.exceptions.UnknownTypeException;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;
import ru.practicum.yandex.telemetry.collector.service.handlers.hub.HubEventHandler;
import ru.practicum.yandex.telemetry.collector.service.handlers.sensor.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    Map<HubEventType, HubEventHandler> hubEventHandlers;
    Map<SensorEventType, SensorEventHandler> sensorEventHandlers;

    public EventServiceImpl(List<HubEventHandler> hubEventHandlers,
                            List<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getType, Function.identity()));
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getType, Function.identity()));
    }

    @Override
    public void postSensorEvent(SensorEvent event) {
        log.info("New sensor event collected by EventService.");
        SensorEventHandler handler = sensorEventHandlers.get(event.getType());
        if (handler == null) {
            log.warn("No handler found for sensor event type: {}.", event.getType());
            throw new UnknownTypeException(String.format("Unknown sensor event type: %s.", event.getType()));
        }
        handler.handle(event);
    }

    @Override
    public void postHubEvent(HubEvent event) {
        log.info("New hub event collected by EventService.");
        HubEventHandler handler = hubEventHandlers.get(event.getType());
        if (handler == null) {
            log.warn("No handler found for hub event type: {}.", event.getType());
            throw new UnknownTypeException(String.format("Unknown hub event type: %s.", event.getType()));
        }
        handler.handle(event);
    }
}
