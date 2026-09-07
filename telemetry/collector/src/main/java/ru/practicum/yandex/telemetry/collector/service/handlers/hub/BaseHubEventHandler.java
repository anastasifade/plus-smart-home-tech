package ru.practicum.yandex.telemetry.collector.service.handlers.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.yandex.telemetry.collector.exceptions.UnknownTypeException;
import ru.practicum.yandex.telemetry.collector.mapper.HubEventMapper;
import ru.practicum.yandex.telemetry.collector.messaging.KafkaEventProducer;
import ru.practicum.yandex.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected KafkaEventProducer producer;

    protected abstract T toAvro(HubEvent event);

    public void handle(HubEvent event) {
        if (!event.getType().equals(getType())) {
            log.warn("Unknown event type in HubEventHandler. Type received: {}. Type expected: {}.",
                    event.getType(), getType());
            throw new UnknownTypeException(String.format("Unknown event type: %s. Type expected: %s.",
                    event.getType(), getType()));
        }

        T payload = toAvro(event);
        HubEventAvro avro = HubEventMapper.toAvro(event, payload);
        producer.send(avro);
    }
}
