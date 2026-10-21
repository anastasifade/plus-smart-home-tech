package ru.yandex.practicum.order.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.common.ReserveRequest;
import ru.yandex.practicum.common.ReserveResponse;
import ru.yandex.practicum.order.exception.BusinessException;
import ru.yandex.practicum.order.exception.ServiceUnavailableException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InventoryClientFallbackFactory implements FallbackFactory<InventoryClient> {
    @Override
    public InventoryClient create(Throwable cause) {
        if (cause instanceof BusinessException be) {
            throw be;
        }

        return new InventoryClient() {
            @Override
            public Set<ReserveResponse> reserveStock(Set<ReserveRequest> requests) {
                List<Long> productIds = toProductIds(requests);
                log.warn(
                        "Inventory service unavailable when requesting to reserve items with ids: {}",
                        productIds,
                        cause
                );

                throw new ServiceUnavailableException(("External service failure when reserving items with ids=" +
                        productIds), cause);
            }

            @Override
            public Set<ReserveResponse> releaseItems(Set<ReserveRequest> requests) {
                List<Long> productIds = toProductIds(requests);
                log.warn(
                        "Inventory service unavailable when requesting to release reservation on items with ids: {}",
                        productIds,
                        cause
                );

                throw new ServiceUnavailableException(("External service failure when requesting to release " +
                        "reservation on items with ids=" + productIds), cause);
            }
        };
    }

    private List<Long> toProductIds(Collection<ReserveRequest> requests) {
        return requests.stream()
                .map(ReserveRequest::productId)
                .toList();
    }
}
