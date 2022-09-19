package com.example.event;

import com.example.datacollector.core.DataField;

import java.util.Set;

public record CollectEventResponseV1(String orderId, DataField source, Set<DataField> destination, Object sourceValue, Set<DestinationData> destinationData) {

    public record DestinationData(DataField destination, Object destinationValue) {

    }
}
