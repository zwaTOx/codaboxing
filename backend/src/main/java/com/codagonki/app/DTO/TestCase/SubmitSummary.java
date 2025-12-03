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
    private Long totalExecutionTime;
}