package com.example.datacollector.dto;

import com.example.datacollector.core.DataField;

import java.util.Set;

public record ExecuteResponseDTO(Set<ResultDTO> result) {

    public record ResultDTO(Set<DataDTO> data) {

        public record DataDTO(DataField dataField, Object value) {

        }
    }
}
