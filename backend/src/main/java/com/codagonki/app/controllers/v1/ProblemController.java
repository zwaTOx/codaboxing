package com.codagonki.app.controllers.v1;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codagonki.app.DTO.Problem.ProblemResponse;
import com.codagonki.app.DTO.Problem.SubmitRequest;
import com.codagonki.app.DTO.TestCase.TestCaseListResponse;
import com.codagonki.app.dependencies.CurrentUser;
import com.codagonki.app.models.User;
import com.codagonki.app.services.ProblemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping("/duels/{duelId}/problems")
    public ResponseEntity<List<ProblemResponse>> getDuelProblems(
            @CurrentUser User user,
            @PathVariable Long duelId) {
        List<ProblemResponse> problems = problemService.getDuelProblems(duelId);
        return ResponseEntity.ok(problems);
    }

    @PostMapping("/duels/{duelId}/problems/{problemId}/submit")
    public ResponseEntity<TestCaseListResponse> submitSolution(
            @CurrentUser User user,
            @PathVariable Long duelId,
            @PathVariable Long problemId,
            @Valid @RequestBody SubmitRequest submitRequest
    ) {
        TestCaseListResponse responseData = problemService.submitSolution(
            user, duelId, problemId, submitRequest
        );
        return ResponseEntity.ok(responseData);
    }
}
