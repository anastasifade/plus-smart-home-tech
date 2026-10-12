package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.exception.NotFoundException;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PriceCalculator calculator;
    private final OrderRepository orderRepository;

    @Override
    public List<OrderDto> findAll() {
        log.debug("Order service: request to find all orders.");
        return toDtos(orderRepository.findAll());
    }

    @Override
    public List<OrderDto> findByClient(String email) {
        log.debug("Order service: request to find all orders for client [email = {}].", email);
        return toDtos(orderRepository.findByCustomerEmail(email));
    }

    @Override
    public OrderDto findById(Long orderId) {
        log.debug("Order service: request to find order [id = {}].", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order [id=%d] not found.", orderId)));
        return OrderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        log.debug("Order service: request to create a new order.");

        Order order = OrderMapper.toOrder(request);
        order.setTotalPrice(calculator.calculateTotal(order.getItems()));
        orderRepository.save(order);

        log.info("New order created successfully. Order id: {}. Total price: {}.",
                order.getId(), order.getTotalPrice());
        return OrderMapper.toDto(order);
    }

    private List<OrderDto> toDtos(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::toDto)
                .toList();
    }
}
