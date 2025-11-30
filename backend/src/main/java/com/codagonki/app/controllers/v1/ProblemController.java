package com.codagonki.app.controllers.v1;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codagonki.app.DTO.Problem.ProblemResponse;
import com.codagonki.app.dependencies.CurrentUser;
import com.codagonki.app.models.User;
import com.codagonki.app.services.ProblemService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
