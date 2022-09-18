package com.example.datacollector.core;

import com.example.datacollector.dto.ExecuteRequestDTO;
import com.example.datacollector.dto.ExecuteResponseDTO;
import com.example.datacollector.order.OrderRequest;
import com.example.datacollector.order.OrderResult;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExecuteMapper {

    public Set<OrderRequest> asOrderRequests(ExecuteRequestDTO request) {
        return request.orders().stream()
                .map(order -> new OrderRequest(order.data().stream().map(data -> new Data(data.dataField(), data.value())).collect(Collectors.toSet())))
                .collect(Collectors.toSet());
    }

    public Set<DataField> asOutput(ExecuteRequestDTO request) {
        return request.model().output().stream().map(ExecuteRequestDTO.ModelDTO.DataModelDTO::dataField).collect(Collectors.toSet());
    }

    public Set<DataField> asInput(ExecuteRequestDTO request) {
        return request.model().input().stream().map(ExecuteRequestDTO.ModelDTO.DataModelDTO::dataField).collect(Collectors.toSet());
    }

    public ExecuteResponseDTO asExecuteResponseDTO(Set<OrderResult> domainResult) {
        var result = domainResult.stream()
                .map(this::asResult)
                .collect(Collectors.toSet());

        return new ExecuteResponseDTO(result);
    }

    private ExecuteResponseDTO.ResultDTO asResult(OrderResult orderResult) {
        var data = orderResult.getResult().stream()
                .map(x -> new ExecuteResponseDTO.ResultDTO.DataDTO(x.getDataField(), x.getValue()))
                .collect(Collectors.toSet());

        return new ExecuteResponseDTO.ResultDTO(data);
    }
}
