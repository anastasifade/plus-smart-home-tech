package ru.yandex.practicum.inventory.service;

import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;

import java.util.List;

public interface InventoryService {
    List<InventoryDto> findAll();

    InventoryDto findByProductId(Long productId);

    InventoryDto createInventoryItem(UpdateInventoryRequest request);

    InventoryDto updateInventoryItem(UpdateInventoryRequest request);

    ReserveResponse reserveItem(ReserveRequest request);
}
