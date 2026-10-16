package ru.yandex.practicum.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.common.ReserveRequest;

@UtilityClass
public class ReserveRequestMapper {

    public ReserveRequest toRequest(Long productId, Integer quantity) {
        return new ReserveRequest(productId, quantity);
    }
}
