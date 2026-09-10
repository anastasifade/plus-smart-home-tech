package ru.practicum.yandex.telemetry.collector.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.practicum.yandex.telemetry.collector.constants.KafkaTopics;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class KafkaEventProducer implements AutoCloseable {

    private final Producer<String, SpecificRecordBase> producer;

    public Future<RecordMetadata> send(HubEventAvro msg) {
        return send(KafkaTopics.HUB_EVENTS_TOPIC, msg);
    }

    public Future<RecordMetadata> send(SensorEventAvro msg) {
        return send(KafkaTopics.SENSOR_EVENTS_TOPIC, msg);
    }

    private Future<RecordMetadata> send(String topic, SpecificRecordBase msg) {
        return producer.send(new ProducerRecord<>(topic, msg));
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofMillis(1000));
        log.info("Kafka producer closed.");
    }
}
