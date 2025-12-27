package com.codagonki.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codagonki.app.DTO.Duel.DuelResultResponse;
import com.codagonki.app.models.Duel;
import com.codagonki.app.models.DuelResult;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.DuelResultRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DuelResultService {
    private final DuelResultRepository duelResultRepository;

    public List<DuelResult> createDuelResults(
            User winner, User loser, Duel duel, Integer gainedRating, Integer lostRating){
        DuelResult winnerResult = DuelResult.builder()
                .user(winner)
                .opponent(loser)
                .duel(duel)
                .resultType(DuelResult.DuelResultType.WIN)
                .gainedRating(gainedRating)
                .enemyGainedRating(lostRating)
                .build();
        
        DuelResult loserResult = DuelResult.builder()
                .user(loser)
                .opponent(winner)
                .duel(duel)
                .resultType(DuelResult.DuelResultType.LOSS)
                .gainedRating(lostRating)
                .enemyGainedRating(gainedRating)
                .build();
        
        duelResultRepository.save(winnerResult);
        duelResultRepository.save(loserResult);
        return List.of(winnerResult, loserResult);
    }

    public List<DuelResultResponse> getUserDuelResults(User user) {
        List<DuelResult> duelResults = duelResultRepository.findByUserId(user.getId());
        
        return duelResults.stream()
                .map(DuelResultResponse::new)
                .collect(Collectors.toList());
    }
}
