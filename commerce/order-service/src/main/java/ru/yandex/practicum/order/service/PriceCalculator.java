package ru.yandex.practicum.order.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.entity.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PriceCalculator {

    private static final int SCALE = 2;

    public BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(this::getProductTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal getProductTotal(OrderItem item) {
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
