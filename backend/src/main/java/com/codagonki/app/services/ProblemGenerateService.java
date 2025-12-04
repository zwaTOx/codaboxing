package com.codagonki.app.services;

import java.util.List;

import org.springframework.stereotype.Service;

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
    public boolean generateRandomProblems(Duel duel, int count) {
        if (problemRepository.getProblemCount(duel.getId()) != 0){
            return false;
        }
        List<Problem> problems = problemRepository.findRandomProblems(count);
        duel.getProblems().clear();
        duel.getProblems().addAll(problems);
        duelRepository.save(duel);
        return true;
    }  
}
