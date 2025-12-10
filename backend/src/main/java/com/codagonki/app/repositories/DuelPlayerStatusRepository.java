package com.codagonki.app.repositories;

import com.codagonki.app.models.DuelPlayerStatus;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuelPlayerStatusRepository extends JpaRepository<DuelPlayerStatus, Long> {

    Optional<DuelPlayerStatus> findByDuelIdAndUserId(Long duelId, Long userId);

    @Transactional
    default DuelPlayerStatus initializePlayer(Long duelId, Long userId, List<Long> problemIds) {
        Map<String, String> progress = new HashMap<>();
        for (Long problemId : problemIds) {
            progress.put(problemId.toString(), DuelPlayerStatus.SolveStatus.NOT_ATTEMPTED.name());
        }
        DuelPlayerStatus newStatus = DuelPlayerStatus.builder()
            .duelId(duelId)
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

    @Transactional
    default boolean update_status(Long userId, Long duelId, Long problemId, DuelPlayerStatus.SolveStatus status) {
        Optional<DuelPlayerStatus> playerStatusOpt = findByDuelIdAndUserId(duelId, userId);
        if (!playerStatusOpt.isPresent()) {
            throw new RuntimeException("Игрок не найден в дуэли");
        }
        DuelPlayerStatus playerStatus = playerStatusOpt.get();
        Map<String, String> progress = playerStatus.getDuelProblemProgress();
        
        progress.put(problemId.toString(), status.name());
        playerStatus.setDuelProblemProgress(progress);
        save(playerStatus);
        return true;
    }
    
    @Transactional
    default boolean updateProblemStatus(Long userId, Long duelId, Long problemId, DuelPlayerStatus.SolveStatus status) {
        return update_status(userId, duelId, problemId, status);
    }
}
