package ru.yandex.practicum.order.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.common.ReserveRequest;
import ru.yandex.practicum.common.ReserveResponse;
import ru.yandex.practicum.order.client.InventoryClient;
import ru.yandex.practicum.order.exception.*;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryGateway {

    private final InventoryClient client;

    public InventoryGatewayResponse reserveItems(Set<ReserveRequest> requests) {
        Set<ReserveResponse> responses;
        try {
            responses = client.reserveStock(requests);
        } catch (NotFoundException e) {
            throw new OrderProcessingException("Some or all products not found in stock.");
        } catch (ConflictException e) {
            throw new OrderProcessingException("Insufficient stock.");
        } catch (ServiceUnavailableException e) {
            return new InventoryGatewayResponse(Set.of(), true);
        }

        log.info("All items successfully reserved.");
        return new InventoryGatewayResponse(responses, false);
    }

    public InventoryGatewayResponse releaseReservations(Set<ReserveRequest> requests) {
        try {
            Set<ReserveResponse> responses = client.releaseItems(requests);
            return new InventoryGatewayResponse(responses, false);
        } catch (Exception e) {
            log.error("Failed to release reservation on items. Requests: {}. Error details: {}.",
                    requests, e.getMessage());
            throw new ReservationFailureException("Failed to release reservation on items.");
        }
    }
}
