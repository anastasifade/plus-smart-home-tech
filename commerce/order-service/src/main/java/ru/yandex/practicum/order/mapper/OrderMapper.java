package ru.yandex.practicum.order.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.order.dto.*;
import ru.yandex.practicum.order.entity.Customer;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.enums.OrderStatus;

import java.util.List;

@UtilityClass
public class OrderMapper {

    public OrderDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems()
                .stream()
                .map(OrderItemMapper::toItemDto)
                .toList();

        return new OrderDto(
                order.getId(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getStatus().toString(),
                order.getTotalPrice(),
                order.getStatusDetails(),
                order.getCreatedAt(),
                items
        );
    }

    public CreateOrderRequest toFullRequest(CreateOrderShortRequest request, List<OrderItemRequest> items) {
        return new CreateOrderRequest(
                request.customerName().trim(),
                request.customerEmail(),
                items
        );
    }

    public Order toOrder(CreateOrderRequest request) {
        Customer customer = new Customer(request.customerName(), request.customerEmail());

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setStatusDetails("Order confirmed");
        order.addItems(request.items()
                .stream()
                .map(OrderItemMapper::toItem)
                .toList());

        return order;
    }

}
