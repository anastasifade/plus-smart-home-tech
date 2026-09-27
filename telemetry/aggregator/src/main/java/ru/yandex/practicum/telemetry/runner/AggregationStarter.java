package ru.yandex.practicum.telemetry.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.config.KafkaAggregatorTopics;
import ru.yandex.practicum.telemetry.messaging.KafkaSnapshotProducer;
import ru.yandex.practicum.telemetry.service.AggregationService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public final class AggregationStarter {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final Consumer<String, SpecificRecordBase> consumer;
    private final KafkaSnapshotProducer producer;
    private final KafkaAggregatorTopics topics;

    @Autowired
    private final AggregationService service;

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(topics.getSensors()));
            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                int count = 0;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    handle(record);
                    manageOffsets(record, count);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Unexpected exception while handling sensor events.", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Closing consumer.");
                consumer.close();
                producer.close();
                log.info("Closing producer.");
            }
        }
    }

    private void manageOffsets(ConsumerRecord<String, SpecificRecordBase> record, int count) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Error when commiting offsets: {}", offsets, exception);
                }
            });
        }
    }

    private void handle(ConsumerRecord<String, SpecificRecordBase> record) {
        log.info("Handling record: topic = {}, partition = {}, offset = {}, value = {}.",
                record.topic(), record.partition(), record.offset(), record.value());

        if (!(record.value() instanceof SensorEventAvro)) {
            log.error("Unknown message type.");
            return;
        }

        Optional<SensorsSnapshotAvro> snapshot = service.handle((SensorEventAvro) record.value());
        if (snapshot.isEmpty()) {
            log.trace("No changes made to the snapshot.");
            return;
        }

        log.debug("Snapshot updated, sending to Kafka.");
        producer.send(snapshot.get());
    }
}
