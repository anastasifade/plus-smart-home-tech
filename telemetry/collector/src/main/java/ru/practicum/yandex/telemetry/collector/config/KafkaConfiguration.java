package ru.practicum.yandex.telemetry.collector.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.yandex.telemetry.collector.messaging.EventAvroSerializer;
import ru.practicum.yandex.telemetry.collector.messaging.KafkaEventProducer;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapConfig;

    @Bean
    public Producer<String, SpecificRecordBase> getProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapConfig);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventAvroSerializer.class.getName());

        return new KafkaProducer<>(config);
    }

    @Bean(destroyMethod = "close")
    public KafkaEventProducer getKafkaProducer(Producer<String, SpecificRecordBase> rawProducer) {
        return new KafkaEventProducer(rawProducer);
    }
}
