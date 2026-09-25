package ru.yandex.practicum.telemetry.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.dal.SensorRepository;
import ru.yandex.practicum.telemetry.mapper.DeviceMapper;
import ru.yandex.practicum.telemetry.model.Sensor;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final SensorRepository sensorRepository;

    @Override
    public void add(String hubId, DeviceAddedEventAvro device) {
        if (sensorRepository.existsByIdAndHubId(device.getId(), hubId)) {
            log.warn("No new device created: device [id = {}] already exists in hub [id = {}].", device.getId(), hubId);
            return;
        }
        Sensor newDevice = DeviceMapper.toSensor(hubId, device.getId());
        sensorRepository.save(newDevice);
        log.info("New device [id = {}] added to hub [id = {}].", device.getId(), hubId);
    }

    @Override
    public void remove(String hubId, DeviceRemovedEventAvro device) {
        if (!sensorRepository.existsByIdAndHubId(device.getId(), hubId)) {
            log.warn("Device [id = {}; hub = {}] does not exist. No device deleted.", device.getId(), hubId);
            return;
        }
        sensorRepository.deleteById(device.getId());
        log.info("Device [id = {}] successfully deleted from hub [id = {}].", device.getId(), hubId);
    }
}
