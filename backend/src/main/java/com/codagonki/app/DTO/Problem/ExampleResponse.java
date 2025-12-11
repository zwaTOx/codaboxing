package com.codagonki.app.DTO.Problem;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExampleResponse {
    
    @JsonProperty("inputData")
    private Map<String, Object> inputData;
    
    @JsonProperty("expectedOutput")
    private Object expectedOutput;
    
    @JsonProperty("description")
    @Builder.Default
    private String description = "";
}