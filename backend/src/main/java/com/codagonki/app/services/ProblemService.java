package com.codagonki.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.DTO.Problem.ProblemResponse;
import com.codagonki.app.DTO.Problem.SubmitRequest;
import com.codagonki.app.DTO.TestCase.SubmitSummary;
import com.codagonki.app.DTO.TestCase.TestCaseListResponse;
import com.codagonki.app.DTO.TestCase.TestCaseResult;
import com.codagonki.app.models.Problem;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.DuelRepository;
import com.codagonki.app.repositories.ProblemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService{
    private final ProblemRepository problemRepository;
    private final DuelRepository duelRepository;
    private final DuelService duelService;
    private final CodeExecutionService codeExecutionService;
    private final DuelActionService duelActionService;

    public List<ProblemResponse> getDuelProblems(Long duelId){
        List <Problem> problems = problemRepository.findByDuelId(duelId);
        if (!duelRepository.existsById(duelId)){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Дуэль не найдена"
            );
        }
        return problems.stream()
            .map(ProblemResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public TestCaseListResponse submitSolution(
            User user, 
            Long duelId, 
            Long problemId, 
            SubmitRequest submitRequest){
        if (!duelService.isUserParticipatingInDuel(user.getId(), duelId)){
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,  
                "Нет доступа к дуэли"
            );
        }
        Optional<Problem> problemOpt = problemRepository.findById(problemId);
        if (problemOpt.isEmpty() || !problemRepository.existsProblemInDuel(duelId, problemId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,  
                "Проблема не найдена в дуэли"
            );
        }
        Problem problem = problemOpt.get();
        String code = submitRequest.getCode();
        double startTime = System.currentTimeMillis();

        List<TestCaseResult> testCaseResults = codeExecutionService.executePythonCode(problemId, code, problem.getFuncName());

        double endTime = System.currentTimeMillis();
        double executionTime = (endTime - startTime)/1000;

        long totalCount = testCaseResults.size();
        long passedCount = testCaseResults.stream()
            .filter(result -> "PASSED".equals(result.getStatus()))
            .count();
        long failedCount = testCaseResults.stream()
            .filter(result -> "FAILED".equals(result.getStatus()))
            .count();
        long skippedCount = testCaseResults.stream()
            .filter(result -> "SKIPPED".equals(result.getStatus()))
            .count();
        long errorCount = testCaseResults.stream()
            .filter(result -> "ERROR".equals(result.getStatus()))
            .count();
        
        if (totalCount == passedCount){
            duelActionService.playSolveAction(user, duelId, problemId);
        } 
        else {
            duelActionService.playAttemptAction(user, duel, problemId);
        }

        SubmitSummary summary = SubmitSummary.builder()
        .total(testCaseResults.size())
        .passed((int) passedCount)
        .failed((int) failedCount)
        .skipped((int) skippedCount)
        .errors((int) errorCount)
        .totalExecutionTime(executionTime) 
        .build();

        TestCaseListResponse response = TestCaseListResponse.builder()
            .results(testCaseResults)
            .summary(summary)
            .build();
        
        return response;
    }
}