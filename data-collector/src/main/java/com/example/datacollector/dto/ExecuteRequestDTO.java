package com.example.datacollector.dto;

import com.example.datacollector.core.DataField;

import java.util.Set;

public record ExecuteRequestDTO(Set<OrderDTO> orders, ModelDTO model) {

    public record OrderDTO(Set<DataDTO> data) {

        public record DataDTO(DataField dataField, Object value) {

        }
    }

    public record ModelDTO(Set<DataModelDTO> input, Set<DataModelDTO> output) {

        public record DataModelDTO(DataField dataField) {
        }
    }
}
