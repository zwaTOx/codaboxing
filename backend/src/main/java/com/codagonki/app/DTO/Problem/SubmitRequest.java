package com.codagonki.app.DTO.Problem;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitRequest {
    private String code;
    @Builder.Default
    private String language = "Python";
    @Builder.Default
    private String version = "3.12";           
    
    // Если пользователь захочет поменять названия входным данным.
    // private Map<String, Object> input; 
}