package com.example.datacollector.core;

import lombok.Value;

@Value
public class SimpleStep {
    private final DataField source;
    private final DataField destination;
}
