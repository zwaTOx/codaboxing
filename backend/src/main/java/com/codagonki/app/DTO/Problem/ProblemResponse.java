package com.codagonki.app.DTO.Problem;

import com.codagonki.app.models.Problem;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponse {
    private Long id;
    private String title;
    private String description;
    @JsonProperty("examples")
    private List<ExampleResponse> examples;
    private Problem.Difficulty difficulty;
    @JsonProperty("hints")
    private List<HintResponse> hints;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProblemResponse(Problem problem) {
        this.id = problem.getId();
        this.title = problem.getTitle();
        this.description = problem.getDescription();
        this.examples = problem.getExamples();
        this.difficulty = problem.getDifficulty();
        this.createdAt = problem.getCreatedAt();
        this.updatedAt = problem.getUpdatedAt();
        this.hints = problem.getHints();
    }
}