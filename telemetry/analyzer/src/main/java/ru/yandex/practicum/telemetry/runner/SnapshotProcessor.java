package ru.yandex.practicum.telemetry.runner;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.config.KafkaAnalyzerClientConfig;
import ru.yandex.practicum.telemetry.config.KafkaAnalyzerTopics;
import ru.yandex.practicum.telemetry.service.snapshot.SnapshotService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public final class SnapshotProcessor extends BaseProcessor {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final SnapshotService snapshotService;

    public SnapshotProcessor(KafkaAnalyzerClientConfig config, KafkaAnalyzerTopics topics,
                             SnapshotService snapshotService) {
        super(config.getSnapshotsConsumer(), List.of(topics.getSnapshots()), CONSUME_ATTEMPT_TIMEOUT);
        this.snapshotService = snapshotService;
    }

    @Override
    protected void handle(ConsumerRecord<String, SpecificRecordBase> record) {
        log.info("Handling record: topic = {}, partition = {}, offset = {}, value = {}.",
                record.topic(), record.partition(), record.offset(), record.value());

        if (!(record.value() instanceof SensorsSnapshotAvro snapshot)) {
            log.error("Unknown record value. No actions performed.");
            return;
        }

        snapshotService.handle(snapshot.getHubId(), snapshot);
    }
}
