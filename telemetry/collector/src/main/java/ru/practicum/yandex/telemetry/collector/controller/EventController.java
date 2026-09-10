package ru.practicum.yandex.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.yandex.telemetry.collector.exceptions.UnknownTypeException;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEventType;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEventType;
import ru.practicum.yandex.telemetry.collector.service.EventService;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService service;

    @PostMapping("/sensors")
    public void postSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("POST /events/sensors request received by EventController.");
        log.debug("Request to post sensor event: {}.", event);
        if (SensorEventType.UNKNOWN_EVENT == event.getType()) {
            log.debug("Sensor event type unknown, failed to save event.");
            throw new UnknownTypeException("Unknown sensor event type.");
        }
        service.postSensorEvent(event);
        log.info("Sensor event saved successfully.");
    }

    @PostMapping("/hubs")
    public void postHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("POST /events/hubs request received by EventController.");
        log.debug("Request to post hub event: {}.", event);
        if (HubEventType.UNKNOWN == event.getType()) {
            log.debug("Hub event type unknown, failed to save event.");
            throw new UnknownTypeException("Unknown hub event type.");
        }
        service.postHubEvent(event);
        log.info("Hub event saved successfully.");
    }
}
