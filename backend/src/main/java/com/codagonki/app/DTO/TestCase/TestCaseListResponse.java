package com.codagonki.app.DTO.TestCase;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseListResponse {
    private List<TestCaseResult> results;
    private SubmitSummary summary;

    public static TestCaseListResponse createFailedResponse(List<Object[]> testCases) {
        List<TestCaseResult> results = testCases.stream()
            .map(tc -> TestCaseResult.builder()
                .input(tc[0])  //kwargs
                .expectedOutput(tc[1])
                .actualOutput(null)
                .status("FAILED")
                .errorMessage("Not executed")
                .build())
            .toList();
        
        return TestCaseListResponse.builder()
            .results(results)
            .summary(SubmitSummary.builder()
                .total(results.size())
                .passed(0)
                .failed(results.size())
                .errors(0)
                .totalExecutionTime(0L)
                .build())
            .build();
    }
}