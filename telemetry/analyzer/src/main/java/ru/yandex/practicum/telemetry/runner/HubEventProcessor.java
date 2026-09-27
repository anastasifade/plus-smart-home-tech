package ru.yandex.practicum.telemetry.runner;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.config.KafkaAnalyzerClientConfig;
import ru.yandex.practicum.telemetry.config.KafkaAnalyzerTopics;
import ru.yandex.practicum.telemetry.service.hub.DeviceService;
import ru.yandex.practicum.telemetry.service.hub.ScenarioService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public final class HubEventProcessor extends BaseProcessor {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final DeviceService deviceService;
    private final ScenarioService scenarioService;

    public HubEventProcessor(KafkaAnalyzerClientConfig config, KafkaAnalyzerTopics topics,
                             DeviceService deviceService, ScenarioService scenarioService) {
        super(config.getHubsConsumer(), List.of(topics.getHubs()), CONSUME_ATTEMPT_TIMEOUT);
        this.deviceService = deviceService;
        this.scenarioService = scenarioService;
    }

    @Override
    protected void handle(ConsumerRecord<String, SpecificRecordBase> record) {
        log.info("Handling record: topic = {}, partition = {}, offset = {}, value = {}.",
                record.topic(), record.partition(), record.offset(), record.value());

        if (!(record.value() instanceof HubEventAvro event)) {
            log.error("Unknown record type. No actions performed.");
            return;
        }

        String hubId = event.getHubId();
        Object payload = event.getPayload();

        switch (payload) {
            case DeviceAddedEventAvro deviceAdded -> deviceService.add(hubId, deviceAdded);
            case DeviceRemovedEventAvro deviceRemoved -> deviceService.remove(hubId, deviceRemoved);
            case ScenarioAddedEventAvro scenarioAdded -> scenarioService.add(hubId, scenarioAdded);
            case ScenarioRemovedEventAvro scenarioRemoved -> scenarioService.remove(hubId, scenarioRemoved);
            default -> log.error("Unknown payload type for hub event record.");
        }
    }
}
