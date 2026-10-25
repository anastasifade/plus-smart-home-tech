package ru.yandex.practicum.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.order.dto.OrderItemDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.entity.OrderItem;

import java.math.RoundingMode;

@UtilityClass
public class OrderItemMapper {

    private static final int SCALE = 2;

    public OrderItemDto toItemDto(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getPrice().setScale(SCALE, RoundingMode.HALF_UP)
        );
    }

    public OrderItemRequest toFullRequest(ProductDto product, int quantity) {
        return new OrderItemRequest(
                product.id(),
                product.name(),
                quantity,
                product.price()
        );
    }

    public OrderItem toItem(OrderItemRequest request) {
        OrderItem item = new OrderItem();
        item.setProductId(request.productId());
        item.setProductName(request.productName().trim());
        item.setQuantity(request.quantity());
        item.setPrice(request.price());
        return item;
    }

}
