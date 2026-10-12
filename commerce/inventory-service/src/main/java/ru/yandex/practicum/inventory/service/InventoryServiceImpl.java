package ru.yandex.practicum.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.entity.Inventory;
import ru.yandex.practicum.inventory.exception.InsufficientStockException;
import ru.yandex.practicum.inventory.exception.NotFoundException;
import ru.yandex.practicum.inventory.mapper.InventoryMapper;
import ru.yandex.practicum.inventory.repository.InventoryRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;

    @Override
    public List<InventoryDto> findAll() {
        log.debug("Inventory service: request to find all inventory items.");
        return toDto(repository.findAll());
    }

    @Override
    public InventoryDto findByProductId(Long productId) {
        log.debug("Inventory service: request to find inventory item for product [id = {}].", productId);
        return InventoryMapper.toDto(findInventoryItemForProduct(productId));
    }

    @Override
    @Transactional
    public InventoryDto createInventoryItem(UpdateInventoryRequest request) {
        log.debug("Inventory service: request to create new inventory item.");

        Inventory item = InventoryMapper.toInventoryItem(request);
        Long id = repository.save(item).getId();

        log.info("New inventory item created, id: {}. Available quantity: {}.", id, getAvailableQuantity(item));
        return InventoryMapper.toDto(item);
    }

    @Override
    @Transactional
    public InventoryDto updateInventoryItem(UpdateInventoryRequest request) {
        log.debug("Inventory service: request to update inventory item [product id = {}], new quantity: {}.",
                request.productId(), request.quantity());

        Inventory item = findInventoryItemForProduct(request.productId());
        item.setQuantity(request.quantity());

        log.info("Inventory item for product [id={}] updated. Total quantity: {}. Available quantity: {}.",
                item.getId(), item.getQuantity(), getAvailableQuantity(item));
        return InventoryMapper.toDto(item);
    }

    @Override
    @Transactional
    public ReserveResponse reserveItem(ReserveRequest request) {
        log.debug("Inventory service: request to reserve product [id = {}], amount to reserve: {}.",
                request.productId(), request.quantity());

        Inventory item = findInventoryItemForProduct(request.productId());
        Integer availableQuantity = getAvailableQuantity(item);

        if (availableQuantity < request.quantity()) {
            log.info("Failed to reserve product [id = {}]: insufficient stock. Requested to reserve: {}. " +
                            "Available: {}.", request.productId(), request.quantity(), availableQuantity);
            throw new InsufficientStockException(String.format("Cannot reserve product [id = %d]: insufficient stock." +
                    "Available amount: %d.", item.getId(), availableQuantity));
        }

        item.setReservedQuantity(item.getReservedQuantity() + request.quantity());
        availableQuantity = getAvailableQuantity(item);

        log.info("Product [id = {}] reserved. Available amount left: {}.", item.getId(), availableQuantity);
        return InventoryMapper.toSuccessfulReserveResponse(availableQuantity);
    }

    private Inventory findInventoryItemForProduct(Long productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Inventory item for product [id=%d] not found.",
                        productId)));
    }

    private List<InventoryDto> toDto(List<Inventory> inventory) {
        return inventory.stream()
                .map(InventoryMapper::toDto)
                .toList();
    }

    private Integer getAvailableQuantity(Inventory item) {
        return item.getQuantity() - item.getReservedQuantity();
    }
}
