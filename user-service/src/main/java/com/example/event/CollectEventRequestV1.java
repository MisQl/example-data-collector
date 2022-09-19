package com.example.event;

import com.example.datacollector.core.DataField;

import java.util.Set;

public record CollectEventRequestV1(String orderId, DataField source, Set<DataField> destination, Object sourceValue) {

    public CollectEventResponseV1 response(Set<CollectEventResponseV1.DestinationData> destinationData){
        return new CollectEventResponseV1(this.orderId, this.source, this.destination, this.sourceValue, destinationData);
    }
}
