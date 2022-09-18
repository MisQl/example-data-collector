package com.example.datacollector.order;

import com.example.datacollector.core.DataField;
import com.example.datacollector.core.SimpleStep;
import com.example.datacollector.order.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    public String collect(OrderRequest orderRequest, Set<List<DataField>> orderSteps) {
        var inputData = orderRequest.getData();
        var simpleSteps = orderSteps.stream()
                .map(this::splitStep)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        var order = Order.newOrder(inputData, simpleSteps);

        order = orderRepository.save(order);
        var orderId = order.getId();

        var availableSimpleSteps = order.poolAvailableSteps();
        availableSimpleSteps.forEach(simpleStepRequest -> eventPublisher.send(orderId, simpleStepRequest.getSource(), simpleStepRequest.getDestination(), simpleStepRequest.getSourceValue()));

        order = orderRepository.save(order);

        return order.getId();
    }

    private List<SimpleStep> splitStep(List<DataField> dataFields) {
        var simpleSteps = new ArrayList<SimpleStep>();
        for (int i = 0; i < dataFields.size() - 1; i++) {
            simpleSteps.add(new SimpleStep(dataFields.get(i), dataFields.get(i + 1)));
        }
        return simpleSteps;
    }
}
