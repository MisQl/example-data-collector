package com.example.datacollector.core;

import com.example.event.CollectEventRequestV1;

import java.util.Set;

public interface DataProvider {

    Set<SupportedMapping> supports();

    Set<Data> get(Data source, Set<DataField> outputModel);

    default boolean supports(CollectEventRequestV1 event) {
        var supportedMappings = supports();
        return supportedMappings.stream()
                .anyMatch(supportedMapping -> supportedMapping.getInputModel().equals(event.source()) && supportedMapping.getOutputModel().containsAll(event.destination()));
    }
}
