package ru.yandex.practicum.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.common.ReserveRequest;
import ru.yandex.practicum.common.ReserveResponse;

import java.util.Set;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PostMapping("/api/inventory/reserve/batch")
    Set<ReserveResponse> reserveStock(@RequestBody Set<ReserveRequest> request);

    @PostMapping("api/inventory/release/batch")
    Set<ReserveResponse> releaseItems(@RequestBody Set<ReserveRequest> request);
}
