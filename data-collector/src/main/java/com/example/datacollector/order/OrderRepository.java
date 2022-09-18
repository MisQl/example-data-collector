package com.example.datacollector.order;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderRepository {

    private static final Map<String, Order> orders = new HashMap<>();

    public Order save(Order order) {
        var orderId = UUID.randomUUID().toString();
        order.setId(orderId);
        orders.put(orderId, order);
        return order;
    }

    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }
}
