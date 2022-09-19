package com.example.datacollector.strategy;

import com.example.datacollector.core.DataField;
import lombok.Value;

import java.util.Set;

@Value
public class Step {

    private final DataField source;
    private final Set<DataField> destination;

    public StepRequest request(Object sourceValue) {
        return new StepRequest(this.source, this.destination, sourceValue);
    }
}
