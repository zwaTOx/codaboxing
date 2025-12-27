package com.codagonki.app.DTO.TestCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResult {
    private Object inputData;  //Map
    private Object expectedOutput;
    @Builder.Default
    private Object actualOutput = null; 
    private String status;  // 'FAILED', 'PASSED', 'ERROR'
    @Builder.Default
    private String errorMessage = null;
    @Builder.Default
    private Float executionTime = null;
}   