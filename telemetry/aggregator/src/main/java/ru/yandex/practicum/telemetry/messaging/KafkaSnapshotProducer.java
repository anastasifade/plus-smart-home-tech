package ru.yandex.practicum.telemetry.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class KafkaSnapshotProducer implements AutoCloseable {

    private final Producer<String, SpecificRecordBase> producer;
    private final String snapshotsTopic;

    public Future<RecordMetadata> send(SensorsSnapshotAvro snapshot) {
        log.debug("Sending snapshot to Kafka: {}.", snapshot);
        return producer.send(new ProducerRecord<>(snapshotsTopic, snapshot));
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofMillis(5000));
    }
}
