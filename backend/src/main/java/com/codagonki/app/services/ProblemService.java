package com.codagonki.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.DTO.Problem.ProblemResponse;
import com.codagonki.app.models.Duel;
import com.codagonki.app.models.Problem;
import com.codagonki.app.repositories.DuelRepository;
import com.codagonki.app.repositories.ProblemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {
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
}