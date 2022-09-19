package com.example.datacollector.executor;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
import com.example.datacollector.order.*;
import com.example.datacollector.strategy.StepsProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExecuteService {

    private final StepsProvider stepsProvider;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @SneakyThrows
    public Set<OrderResult> collectData(Set<DataField> inputModel, Set<DataField> outputModel, Set<OrderRequest> orderRequests) {
        var steps = stepsProvider.calculateSteps(inputModel, outputModel);

        var orderIds = orderRequests.stream()
                .map(OrderRequest::getData)
                .map(inputData -> orderService.collect(inputData, outputModel, steps))
                .collect(Collectors.toSet());

        var isDone = false;
        do { // todo to remove (or refactor)
            isDone = orderIds.stream()
                    .map(orderRepository::findById)
                    .allMatch(o -> o.map(Order::isDone).orElse(false));
            TimeUnit.SECONDS.sleep(1);
        } while (!isDone);

        var result = orderIds.stream()
                .map(orderRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return result.stream()
                .map(order -> new OrderResult(asData(order)))
                .collect(Collectors.toSet());
    }

    private Set<Data> asData(Order order) {
        return order.getOutputData().stream()
                .map(output -> new Data(output.getDataField(), output.getValue()))
                .collect(Collectors.toSet());
    }
}
