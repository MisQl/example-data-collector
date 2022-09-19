package com.example.datacollector.order;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
import com.example.datacollector.order.event.EventPublisher;
import com.example.datacollector.strategy.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public String collect(Set<Data> inputData, Set<DataField> outputModel, List<Step> steps) {
        var order = Order.newOrder(inputData, outputModel, steps);
        order = orderRepository.save(order);

        var orderId = order.getId();
        var availableSteps = order.poolAvailableSteps();
        availableSteps.forEach(stepRequest -> eventPublisher.send(orderId, stepRequest.getSource(), stepRequest.getDestination(), stepRequest.getSourceValue()));
        order = orderRepository.save(order);

        return orderId;
    }
}
