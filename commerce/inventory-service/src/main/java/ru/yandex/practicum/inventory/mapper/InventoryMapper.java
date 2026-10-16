package ru.yandex.practicum.inventory.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.common.ReserveResponse;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.entity.Inventory;

@UtilityClass
public class InventoryMapper {

    public Inventory toInventoryItem(UpdateInventoryRequest request) {
        Inventory inventory = new Inventory();
        inventory.setProductId(request.productId());
        inventory.setQuantity(request.quantity());
        return inventory;
    }

    public InventoryDto toDto(Inventory inventory) {
        return new InventoryDto(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getReservedQuantity(),
                inventory.getQuantity() - inventory.getReservedQuantity()
        );
    }

    public ReserveResponse toSuccessfulReserveResponse(Integer availableQuantity) {
        return new ReserveResponse(
                true,
                availableQuantity,
                "Product successfully reserved."
        );
    }
}
