package ru.yandex.practicum.inventory.service;

import ru.yandex.practicum.common.ReserveRequest;
import ru.yandex.practicum.common.ReserveResponse;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;

import java.util.List;
import java.util.Set;

public interface InventoryService {
    List<InventoryDto> findAll();

    InventoryDto findByProductId(Long productId);

    InventoryDto createInventoryItem(UpdateInventoryRequest request);

    InventoryDto updateInventoryItem(UpdateInventoryRequest request);

    Set<ReserveResponse> reserveItems(Set<ReserveRequest> requests);

    ReserveResponse reserveItem(ReserveRequest request);

    void releaseItems(Set<ReserveRequest> requests);

    void releaseItem(ReserveRequest request);
}
