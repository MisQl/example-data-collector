package com.example.datacollector.dto;

import java.util.Set;

import static com.example.datacollector.core.DataField.*;

public class ResponseMock {

    public static ExecuteResponseDTO executeResponse() {
        return new ExecuteResponseDTO(Set.of(
                new ExecuteResponseDTO.ResultDTO(Set.of(
                        new ExecuteResponseDTO.ResultDTO.DataDTO(USER_FIRSTNAME, "Jan"),
                        new ExecuteResponseDTO.ResultDTO.DataDTO(USER_LASTNAME, "Nowak"),
                        new ExecuteResponseDTO.ResultDTO.DataDTO(ADDRESS_STREET, "ul. Grunwaldzka"),
                        new ExecuteResponseDTO.ResultDTO.DataDTO(USER_PARENT_LASTNAME, "Nowak")
                )),
                new ExecuteResponseDTO.ResultDTO(Set.of(
                        new ExecuteResponseDTO.ResultDTO.DataDTO(USER_FIRSTNAME, "Ola"),
                        new ExecuteResponseDTO.ResultDTO.DataDTO(USER_LASTNAME, "Kowalska"),
                        new ExecuteResponseDTO.ResultDTO.DataDTO(ADDRESS_STREET, "ul. Mickiewicza"),
                        new ExecuteResponseDTO.ResultDTO.DataDTO(USER_PARENT_LASTNAME, "Wisniewski")
                ))
        ));
    }
}
