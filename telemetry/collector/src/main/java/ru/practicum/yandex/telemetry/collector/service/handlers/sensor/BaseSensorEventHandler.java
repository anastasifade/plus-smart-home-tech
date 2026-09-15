package ru.practicum.yandex.telemetry.collector.service.handlers.sensor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.yandex.telemetry.collector.exceptions.UnknownTypeException;
import ru.practicum.yandex.telemetry.collector.mapper.SensorEventMapper;
import ru.practicum.yandex.telemetry.collector.messaging.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    @Autowired
    protected KafkaEventProducer producer;

    protected abstract T toAvro(SensorEventProto event);

    public void handle(SensorEventProto event) {
        if (!event.getPayloadCase().equals(getType())) {
            log.warn("Unknown event type in SensorEventHandler. Type received: {}. Type expected: {}.",
                    event.getPayloadCase(), getType());
            throw new UnknownTypeException(String.format("Unknown event type: %s. Type expected: %s.",
                    event.getPayloadCase(), getType()));
        }

        T payload = toAvro(event);
        SensorEventAvro avro = SensorEventMapper.toAvro(event, payload);
        producer.send(avro);
    }
}
