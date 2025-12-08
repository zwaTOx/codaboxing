package com.codagonki.app.DTO.TestCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitSummary {
    private Integer total;
    private Integer passed;
    private Integer failed;
    private Integer errors;
    private Double totalExecutionTime; 
    
    public Double getRoundedExecutionTime() {
        if (totalExecutionTime == null) return null;
        return Math.round(totalExecutionTime * 100.0) / 100.0;
    }

}