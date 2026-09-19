package ru.practicum.yandex.telemetry.collector.config;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.yandex.telemetry.collector.messaging.KafkaEventProducer;

@Configuration
@RequiredArgsConstructor
public class KafkaCollectorProducerConfig {

    private final KafkaCollectorConfig config;

    @Bean(destroyMethod = "close")
    public Producer<String, SpecificRecordBase> getProducer() {
        return new KafkaProducer<>(config.getProperties());
    }

    @Bean(destroyMethod = "close")
    public KafkaEventProducer getKafkaProducer(Producer<String, SpecificRecordBase> rawProducer) {
        return new KafkaEventProducer(rawProducer, config.getTopics().getHubs(), config.getTopics().getSensors());
    }
}
