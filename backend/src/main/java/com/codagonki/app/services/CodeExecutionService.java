package com.codagonki.app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.codagonki.app.DTO.TestCase.TestCaseResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeExecutionService {
    
    @Value("${python.executor.url:http://localhost:8000/api/v1/compile}")
    private String pythonExecutorUrl;
    
    private final TestCaseService testCaseService;
    private final RestTemplate restTemplate;
    
    public List<TestCaseResult> executePythonCode(Long problemId, String code, String funcName) {
        List<Map<String, Object>> test_cases = testCaseService.getTestCasesAsMap(problemId);
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("code", code);
            requestBody.put("test_cases", test_cases);
            requestBody.put("func_name", funcName);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<TestCaseResult[]> response = restTemplate.exchange(
                pythonExecutorUrl,
                HttpMethod.POST,
                request,
                TestCaseResult[].class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                throw new RuntimeException("Failed to execute Python code.");
            }
            
        } catch (HttpStatusCodeException e) {
            throw e;
            
        } catch (Exception e) {
            throw new RuntimeException("Error executing Python code: " + e.getMessage(), e);
        }
    }
}