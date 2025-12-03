package com.codagonki.app.DTO.TestCase;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class TestCaseResult {
    private Object input;  //Object/Map
    private Object expectedOutput;
    @Builder.Default
    private Object actualOutput = null; 
    private String status;  // 'FAILED', 'PASSED', 'ERROR'
    @Builder.Default
    private String errorMessage = null;
    // private Integer order; 

    public TestCaseResult(Object input, Object expectedOutput, Object actualOutput, 
            String status, String errorMessage) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
        this.status = status;
        this.errorMessage = errorMessage;
    }
}   