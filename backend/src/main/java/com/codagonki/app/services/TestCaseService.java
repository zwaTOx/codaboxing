package com.codagonki.app.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codagonki.app.repositories.TestCaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    
    public List<Map<String, Object>> getTestCasesAsMap(Long problemId) {
        return testCaseRepository.findByProblemId(problemId).stream()
            .map(testCase -> {
                Map<String, Object> map = new HashMap<>();
                map.put("input", testCase.getInput());
                map.put("output", testCase.getOutput());
                return map;
            })
            .collect(Collectors.toList());
    }
}
