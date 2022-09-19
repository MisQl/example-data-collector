package com.example.datacollector.order;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
import com.example.datacollector.strategy.Step;
import com.example.datacollector.strategy.StepRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Order {

    @Getter
    @Setter
    private String id;
    private Set<Data> inputData;
    private Set<Data> outputData;
    private Set<DataField> outputModel;
    private Set<Step> steps;

    public static Order newOrder(Set<Data> inputData, Set<DataField> outputModel, List<Step> steps) {
        var order = new Order();
        order.inputData = new HashSet<>(inputData);
        order.steps = new HashSet<>(steps);
        order.outputModel = new HashSet<>(outputModel);
        order.outputData = new HashSet<>();
        return order;
    }

    public Set<StepRequest> poolAvailableSteps() {
        var availableData = new HashMap<DataField, Object>();
        availableData.putAll(inputData.stream().collect(Collectors.toMap(Data::getDataField, Data::getValue)));
        availableData.putAll(outputData.stream().collect(Collectors.toMap(Data::getDataField, Data::getValue)));
        var availableFields = availableData.keySet();
        var availableSteps = steps.stream()
                .filter(step -> availableFields.contains(step.getSource()))
                .collect(Collectors.toSet());

        steps.removeAll(availableSteps);

        return availableSteps.stream()
                .map(step -> step.request(availableData.get(step.getSource())))
                .collect(Collectors.toSet());
    }

    public void addOutputData(Set<Data> outputData) {
        this.outputData.addAll(outputData);
    }

    public Set<Data> getOutputData() {
        return this.outputData.stream()
                .filter(o -> outputModel.contains(o.getDataField()))
                .collect(Collectors.toSet());
    }

    public boolean isDone() {
        return steps.isEmpty() && outputData.stream()
                .map(Data::getDataField)
                .collect(Collectors.toSet())
                .containsAll(outputModel);
    }
}
