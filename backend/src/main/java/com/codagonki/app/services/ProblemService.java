package com.codagonki.app.services;

import java.util.List;
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
        if (!problemRepository.existsProblemInDuel(duelId, problemId)){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,  
                "Проблема не найдена в дуэли"
            );
        }
        String code = submitRequest.getCode();
        // Testing code in python
        // res = [output]
        // List<Map<String, Object>> results = new ArrayList<>();
        List<TestCaseResult> testCaseResults = codeExecutionService.executePythonCode(problemId, code);
        
        // List<TestCaseResult> testCaseResults = new ArrayList<>();
        // TestCaseResult testCase1 = new TestCaseResult(
        //     Map.of("n", 10),
        //     16,
        //     16,
        //     "PASSED",
        //     null
        // );
        // testCaseResults.add(testCase1);

        // // Тест-кейс 2: ошибка выполнения
        // TestCaseResult testCase2 = new TestCaseResult(
        //     Map.of("n", 0),
        //     0,
        //     null,
        //     "ERROR",
        //     "Division by Zero"
        // );
        // testCaseResults.add(testCase2);

        long passedCount = testCaseResults.stream()
            .filter(result -> "PASSED".equals(result.getStatus()))
            .count();
        long failedCount = testCaseResults.stream()
            .filter(result -> "FAILED".equals(result.getStatus()))
            .count();
        long errorCount = testCaseResults.stream()
            .filter(result -> "ERROR".equals(result.getStatus()))
            .count();
        

        SubmitSummary summary = SubmitSummary.builder()
        .total(testCaseResults.size())
        .passed((int) passedCount)
        .failed((int) failedCount)
        .errors((int) errorCount)
        .totalExecutionTime(0L) 
        .build();

        TestCaseListResponse response = TestCaseListResponse.builder()
            .results(testCaseResults)
            .summary(summary)
            .build();
        
        return response;
    }
}