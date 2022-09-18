package com.example.datacollector.core;

import com.example.datacollector.dto.ExecuteRequestDTO;
import com.example.datacollector.dto.ExecuteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExecuteController {

    private final ExecuteService executeService;
    private final ExecuteMapper executeMapper;

    @PostMapping("/execute")
    public ExecuteResponseDTO execute(@RequestBody ExecuteRequestDTO request) {
        var inputModel = executeMapper.asInput(request);
        var outputModel = executeMapper.asOutput(request);
        var orderRequests = executeMapper.asOrderRequests(request);

        var result = executeService.collectData(inputModel, outputModel, orderRequests);

        return executeMapper.asExecuteResponseDTO(result);
    }


}
