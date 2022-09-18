package com.example.datacollector.order;

import com.example.datacollector.core.Data;
import com.example.datacollector.core.DataField;
import com.example.datacollector.core.SimpleStep;
import com.example.datacollector.core.SimpleStepRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Order {

    @Getter
    @Setter
    private String id;
    private Set<Data> inputData;
    private Set<Data> outputData;
    private Set<DataField> outputModel;
    private Set<SimpleStep> simpleSteps;

    public static Order newOrder(Set<Data> inputData, Set<DataField> outputModel, Set<SimpleStep> simpleSteps) {
        var order = new Order();
        order.inputData = new HashSet<>(inputData);
        order.simpleSteps = new HashSet<>(simpleSteps);
        order.outputModel = new HashSet<>(outputModel);
        order.outputData = new HashSet<>();
        return order;
    }

    public void addOutputData(DataField outputField, Object outputValue) {
        outputData.add(new Data(outputField, outputValue));
    }

    public Set<SimpleStepRequest> poolAvailableSteps() {
        var availableData = new HashMap<DataField, Object>();
        availableData.putAll(inputData.stream().collect(Collectors.toMap(Data::getDataField, Data::getValue)));
        availableData.putAll(outputData.stream().collect(Collectors.toMap(Data::getDataField, Data::getValue)));
        var availableFields = availableData.keySet();
        var availableSimpleSteps = simpleSteps.stream()
                .filter(step -> availableFields.contains(step.getSource()))
                .collect(Collectors.toSet());

        simpleSteps.removeAll(availableSimpleSteps);

        return availableSimpleSteps.stream()
                .map(simpleStep -> new SimpleStepRequest(simpleStep.getSource(), simpleStep.getDestination(), availableData.get(simpleStep.getSource())))
                .collect(Collectors.toSet());
    }

    public boolean isDone() {
        return simpleSteps.isEmpty() && outputData.stream()
                .map(Data::getDataField)
                .collect(Collectors.toSet())
                .containsAll(outputModel);
    }

    public Set<Data> getOutputData() {
        return this.outputData.stream()
                .filter(o -> outputModel.contains(o.getDataField()))
                .collect(Collectors.toSet());
    }
}
