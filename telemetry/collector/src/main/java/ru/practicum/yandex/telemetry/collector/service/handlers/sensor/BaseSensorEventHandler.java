package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.yandex.telemetry.collector.exceptions.UnknownTypeException;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.practicum.yandex.telemetry.collector.messaging.KafkaEventProducer;
import ru.practicum.yandex.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected KafkaEventProducer producer;

    protected abstract T toAvro(SensorEvent event);

    public void handle(SensorEvent event) {
        if (!event.getType().equals(getType())) {
            log.warn("Unknown event type in SensorEventHandler. Type received: {}. Type expected: {}.",
                    event.getType(), getType());
            throw new UnknownTypeException(String.format("Unknown event type: %s. Type expected: %s.",
                    event.getType(), getType()));
        }

        T payload = toAvro(event);
        SensorEventAvro avro = SensorEventMapper.toAvro(event, payload);
        producer.send(avro);
    }
}
