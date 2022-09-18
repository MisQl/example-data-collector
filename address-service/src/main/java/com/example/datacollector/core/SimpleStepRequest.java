package com.example.datacollector.core;

import lombok.Value;

@Value
public class SimpleStepRequest {

    private final DataField source;
    private final DataField destination;
    private final Object sourceValue;
}
