package com.example.event;

import com.example.datacollector.core.DataField;

public record CollectEventRequestV1(String orderId, DataField source, DataField destination, Object sourceValue) {
}
