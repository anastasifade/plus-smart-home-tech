package ru.yandex.practicum.telemetry.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseProcessor implements Runnable {

    private final Consumer<String, SpecificRecordBase> consumer;
    private final List<String> topics;
    private final Duration timeout;


    protected final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(topics);
            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(timeout);
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
            log.error("Unexpected exception.", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Closing consumer.");
                consumer.close();
            }
        }
    }

    protected void manageOffsets(ConsumerRecord<String, SpecificRecordBase> record, int count) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Error when commiting offsets: {}.", offsets, exception);
                }
            });
        }
    }

    abstract void handle(ConsumerRecord<String, SpecificRecordBase> record);
}
