package com.codagonki.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.models.Duel;
import com.codagonki.app.models.DuelPlayerStatus;
import com.codagonki.app.models.Problem;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.DuelPlayerStatusRepository;
import com.codagonki.app.repositories.DuelRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DuelActionService {
    private final DuelPlayerStatusRepository duelPlayerStatusRepository;
    private final DuelRepository duelRepository;

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

    public boolean playSolveAction(User user, Long duelId, Long problemId){
        try{
            DuelPlayerStatus duelPlayerStatus = duelPlayerStatusRepository.updateProblemStatus(user.getId(), duelId, problemId, DuelPlayerStatus.SolveStatus.SOLVED);
            Map<String, String> progress = duelPlayerStatus.getDuelProblemProgress();
            boolean is_player_win = progress.values().stream()
                .allMatch(status -> 
                    DuelPlayerStatus.SolveStatus.SOLVED.name().equals(status)
                );
            //WS sent
            // game end
            duelRepository.completeDuel(duelId);
            return is_player_win;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Не удалось обновить статус задачи: " + e.getMessage()
            );
        }

    }

    public void playAttemptAction(User user, Long duelId, Long problemId){
        try{
            DuelPlayerStatus duelPlayerStatus = duelPlayerStatusRepository.updateProblemStatus(user.getId(), duelId, problemId, DuelPlayerStatus.SolveStatus.ATTEMPTED);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Не удалось обновить статус задачи: " + e.getMessage()
            );
        }
    }
}
