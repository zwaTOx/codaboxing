package com.codagonki.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.models.Duel;
import com.codagonki.app.models.DuelPlayerStatus;
import com.codagonki.app.models.Problem;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.DuelPlayerStatusRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DuelActionService {
    private final DuelPlayerStatusRepository duelPlayerStatusRepository;

    public boolean setStartStatus(Duel duel, List<Problem> problems){
        List<Long> problemIds = problems.stream()
                .map(Problem::getId)
                .toList();
            
        List<Long> userIds = new ArrayList<>();
        userIds.add(duel.getHostUserId());
        userIds.add(duel.getConnectingUserId());

        List<DuelPlayerStatus> playerStatuses = duelPlayerStatusRepository
            .initializePlayers(duel.getId(), userIds, problemIds);
        return true;
    }

    public void playSolveAction(User user, Long duelId, Long problemId){
        try{
            duelPlayerStatusRepository.updateProblemStatus(user.getId(), duelId, problemId, DuelPlayerStatus.SolveStatus.SOLVED);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Не удалось обновить статус задачи: " + e.getMessage()
            );
        }
    }
}
