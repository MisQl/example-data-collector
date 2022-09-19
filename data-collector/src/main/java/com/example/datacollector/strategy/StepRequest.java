package com.example.datacollector.strategy;

import com.example.datacollector.core.DataField;
import lombok.Value;

import java.util.Set;

@Value
public class StepRequest {

    private final DataField source;
    private final Set<DataField> destination;
    private final Object sourceValue;
}
