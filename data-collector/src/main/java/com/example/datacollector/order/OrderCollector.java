package com.example.datacollector.order;

import com.example.datacollector.core.Data;
import com.example.datacollector.order.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrderCollector {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public void updateOrder(String orderId, Set<Data> outputData) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order doesn't exist: " + orderId));
        order.addOutputData(outputData);

        if (!order.isDone()) {
            var availableSimpleSteps = order.poolAvailableSteps();
            availableSimpleSteps.forEach(simpleStepRequest -> eventPublisher.send(orderId, simpleStepRequest.getSource(), simpleStepRequest.getDestination(), simpleStepRequest.getSourceValue()));
            orderRepository.save(order);
        } else {
            System.out.println("---> ORDER IS DONE: " + orderId);
        }
    }
}
