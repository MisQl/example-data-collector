package com.example.datacollector.order;

import com.example.datacollector.core.Data;
import lombok.Value;

import java.util.Set;

@Value
public class OrderResult {

    private final Set<Data> result;
}
