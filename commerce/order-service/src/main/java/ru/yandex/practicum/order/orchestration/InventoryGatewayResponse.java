package ru.yandex.practicum.order.orchestration;

import ru.yandex.practicum.common.ReserveResponse;

import java.util.Set;

public record InventoryGatewayResponse(
        Set<ReserveResponse> responses,
        boolean isDegraded
) {
}
