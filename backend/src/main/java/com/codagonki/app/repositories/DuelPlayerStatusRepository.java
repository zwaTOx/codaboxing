package com.codagonki.app.repositories;

import com.codagonki.app.models.DuelPlayerStatus;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuelPlayerStatusRepository extends JpaRepository<DuelPlayerStatus, Long> {

    @Transactional
    default DuelPlayerStatus initializePlayer(Long duelId, Long userId, List<Long> problemIds) {
        Map<String, Object> progress = new HashMap<>();
        for (Long problemId : problemIds) {
            progress.put(problemId.toString(), DuelPlayerStatus.SolveStatus.NOT_ATTEMPTED.name());
        }
        DuelPlayerStatus newStatus = DuelPlayerStatus.builder()
            .duel(null)
            .userId(userId)
            .duelProblemProgress(progress)
            .build();
        
        return save(newStatus);
    }

    @Transactional
    default List<DuelPlayerStatus> initializePlayers(Long duelId, List<Long> userIds, List<Long> problemIds) {
        return userIds.stream()
            .map(userId -> initializePlayer(duelId, userId, problemIds))
            .toList();
    }
}
