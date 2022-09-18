package com.example.event;

import com.example.datacollector.core.DataField;

public record CollectEventResponseV1(String orderId, DataField source, DataField destination, Object sourceValue, Object destinationValue) {
}
