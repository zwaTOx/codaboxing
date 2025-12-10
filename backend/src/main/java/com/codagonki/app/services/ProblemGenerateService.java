package com.codagonki.app.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.models.Duel;
import com.codagonki.app.models.Problem;
import com.codagonki.app.repositories.DuelRepository;
import com.codagonki.app.repositories.ProblemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemGenerateService {
    private final ProblemRepository problemRepository;
    private final DuelRepository duelRepository;

    @Transactional
    public List<Problem> generateRandomProblems(Duel duel) {
        if (problemRepository.getProblemCount(duel.getId()) != 0){
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,  
                "Невозможно подобрать проблемы для дуэли"
            );
        }
        List<Problem> problems = problemRepository.findRandomProblems(duel.getProblemCount());
        duel.getProblems().clear();
        duel.getProblems().addAll(problems);
        duelRepository.save(duel);
        return problems;
    }  
}
