package ru.yandex.practicum.order;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.common.ProductDto;
import ru.yandex.practicum.common.ReserveResponse;
import ru.yandex.practicum.order.client.InventoryClient;
import ru.yandex.practicum.order.client.ProductClient;
import ru.yandex.practicum.order.dto.CreateOrderShortRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemShortRequest;
import ru.yandex.practicum.order.enums.OrderStatus;
import ru.yandex.practicum.order.exception.OrderProcessingException;
import ru.yandex.practicum.order.orchestration.OrderOrchestrator;
import ru.yandex.practicum.order.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderOrchestrationTests {

    private static final String NAME = "username";
    private static final String EMAIL = "user@email.test";
    private static final Long PRODUCT_ID = 1L;
    private static final Integer QUANTITY = 2;
    private static final ProductDto ACTIVE_PRODUCT = new ProductDto(PRODUCT_ID, "product",
            "test product", BigDecimal.valueOf(14.99), null, "path/image.png", true);
    private static final ReserveResponse SUCCESSFUL_RESERVE_RESPONSE =
            new ReserveResponse(true, 100, "test message");

    @MockBean
    private final ProductClient productClient;
    @MockBean
    private final InventoryClient inventoryClient;
    @SpyBean
    private final OrderService service;

    private final OrderOrchestrator orchestrator;

    @Test
    void test_OrderIsCreated_forValidRequest() {
        OrderItemShortRequest itemRequest = new OrderItemShortRequest(PRODUCT_ID, QUANTITY);
        CreateOrderShortRequest orderRequest = new CreateOrderShortRequest(NAME, EMAIL, List.of(itemRequest));

        when(productClient.getProductsById(Set.of(PRODUCT_ID)))
                .thenReturn(Set.of(ACTIVE_PRODUCT));
        when(inventoryClient.reserveStock(any()))
                .thenReturn(Set.of(SUCCESSFUL_RESERVE_RESPONSE));

        OrderDto orderDto = orchestrator.createOrder(orderRequest);
        assertThat(orderDto.id(), notNullValue());
        assertThat(orderDto.status(), is(OrderStatus.CONFIRMED.toString()));

        verify(productClient).getProductsById(Set.of(PRODUCT_ID));
        verify(inventoryClient).reserveStock(any());
        verify(inventoryClient, never()).releaseItems(any());
    }

    @Test
    void test_forDuplicateProduct_SumsQuantity_CreatesOrderForOneProduct() {
        OrderItemShortRequest itemRequest = new OrderItemShortRequest(PRODUCT_ID, QUANTITY);
        CreateOrderShortRequest orderRequest =
                new CreateOrderShortRequest(NAME, EMAIL, List.of(itemRequest, itemRequest));

        when(productClient.getProductsById(Set.of(PRODUCT_ID)))
                .thenReturn(Set.of(ACTIVE_PRODUCT));
        when(inventoryClient.reserveStock(any()))
                .thenReturn(Set.of(SUCCESSFUL_RESERVE_RESPONSE));

        OrderDto order = orchestrator.createOrder(orderRequest);
        assertThat(order.id(), notNullValue());
        assertThat(order.items().size(), is(1));
        assertThat(order.items().getFirst().quantity(), is(QUANTITY * 2));

        verify(productClient).getProductsById(any());
        verifyNoMoreInteractions(productClient);

        verify(inventoryClient).reserveStock(any());
        verifyNoMoreInteractions(inventoryClient);
    }

    @Test
    void test_productInactive_noReservationMade_noOrderCreated() {
        OrderItemShortRequest itemRequest = new OrderItemShortRequest(PRODUCT_ID, QUANTITY);
        CreateOrderShortRequest orderRequest =
                new CreateOrderShortRequest(NAME, EMAIL, List.of(itemRequest, itemRequest));

        ProductDto inactiveProduct = new ProductDto(PRODUCT_ID, "test", "test",
                BigDecimal.valueOf(14.55), null, "path/img.jpeg", false);

        when(productClient.getProductsById(Set.of(PRODUCT_ID)))
                .thenReturn(Set.of(inactiveProduct));

        List<OrderDto> ordersBefore = service.findAll();

        assertThrows(OrderProcessingException.class, () -> orchestrator.createOrder(orderRequest));

        List<OrderDto> ordersAfter = service.findAll();
        assertThat(ordersBefore.size(), is(ordersAfter.size()));

        verifyNoInteractions(inventoryClient);
    }

    @Test
    void test_reservationFailure_noOrderCreated() {
        OrderItemShortRequest itemRequest = new OrderItemShortRequest(PRODUCT_ID, QUANTITY);
        CreateOrderShortRequest orderRequest = new CreateOrderShortRequest(NAME, EMAIL, List.of(itemRequest));

        when(productClient.getProductsById(Set.of(PRODUCT_ID)))
                .thenReturn(Set.of(ACTIVE_PRODUCT));
        when(inventoryClient.reserveStock(any()))
                .thenThrow(OrderProcessingException.class);

        List<OrderDto> ordersBefore = service.findAll();

        assertThrows(OrderProcessingException.class, () -> orchestrator.createOrder(orderRequest));

        List<OrderDto> ordersAfter = service.findAll();
        assertThat(ordersBefore.size(), is(ordersAfter.size()));

        verify(inventoryClient, never()).releaseItems(any());
    }

    @Test
    void test_failureAfterSuccessfulReservation_reservationReleased_noOrderCreated() {
        OrderItemShortRequest itemRequest = new OrderItemShortRequest(PRODUCT_ID, QUANTITY);
        CreateOrderShortRequest orderRequest = new CreateOrderShortRequest(NAME, EMAIL, List.of(itemRequest));

        when(productClient.getProductsById(Set.of(PRODUCT_ID)))
                .thenReturn(Set.of(ACTIVE_PRODUCT));
        when(inventoryClient.reserveStock(any()))
                .thenReturn(Set.of(SUCCESSFUL_RESERVE_RESPONSE));
        doThrow(RuntimeException.class)
                .when(service).createOrder(any());

        List<OrderDto> ordersBefore = service.findAll();

        assertThrows(RuntimeException.class, () -> orchestrator.createOrder(orderRequest));

        List<OrderDto> ordersAfter = service.findAll();
        assertThat(ordersBefore.size(), is(ordersAfter.size()));

        verify(productClient).getProductsById(any());
        verify(inventoryClient).reserveStock(any());
        verify(inventoryClient).releaseItems(any());
    }

}
