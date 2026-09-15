package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.yandex.telemetry.collector.exceptions.UnknownTypeException;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.practicum.yandex.telemetry.collector.messaging.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    @Autowired
    protected KafkaEventProducer producer;

    protected abstract T toAvro(HubEventProto event);

    public void handle(HubEventProto event) {
        if (!event.getPayloadCase().equals(getType())) {
            log.warn("Unknown event type in HubEventHandler. Type received: {}. Type expected: {}.",
                    event.getPayloadCase(), getType());
            throw new UnknownTypeException(String.format("Unknown event type: %s. Type expected: %s.",
                    event.getPayloadCase(), getType()));
        }

        T payload = toAvro(event);
        HubEventAvro avro = HubEventMapper.toAvro(event, payload);
        producer.send(avro);
    }
}
