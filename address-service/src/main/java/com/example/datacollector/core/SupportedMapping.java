package com.example.datacollector.core;

import lombok.Value;

import java.util.Set;

@Value
public class SupportedMapping {

    private final DataField inputModel;
    private final Set<DataField> outputModel;
}
