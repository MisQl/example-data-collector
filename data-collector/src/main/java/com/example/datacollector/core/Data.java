package com.example.datacollector.core;

import lombok.Value;

@Value
public class Data {

    private final DataField dataField;
    private final Object value;
}
