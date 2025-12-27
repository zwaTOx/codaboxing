package com.codagonki.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.DTO.WsMessage;
import com.codagonki.app.models.Duel;
import com.codagonki.app.models.DuelPlayerStatus;
import com.codagonki.app.models.Problem;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.DuelPlayerStatusRepository;
import com.codagonki.app.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DuelActionService {
    private final DuelPlayerStatusRepository duelPlayerStatusRepository;
    private final WebSocketService webSocketService;
    private final UserRepository userRepository;
    private final StatService statService;

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

    public Integer winAction(User user){
        int currentWins = user.getWins() + 1;
        user.setWins(currentWins);
        int currentStreak = user.getCurrentWinStreak() + 1;
        user.setCurrentWinStreak(currentStreak);
        if (currentStreak > user.getMaxWinStreak()) {
            user.setMaxWinStreak(currentStreak);
        }
        int totalGames = user.getTotalGames() + 1;
        user.setTotalGames(totalGames);
        int losses = user.getLosses();
        int gainedRating = statService.calculateNewRatingAfterWin();
        int newRating = gainedRating + user.getRating();
        user.setRating(newRating);
        if (newRating > user.getMaxRating()) {
            user.setMaxRating(newRating);
        }
        double winRate = statService.calculateWinRate(currentWins, totalGames);
        user.setWinRate(winRate);
        userRepository.save(user);
        return gainedRating;
    }

    public Integer loseAction(User user) {
        int currentLosses = user.getLosses() + 1;
        user.setLosses(currentLosses);
        user.setCurrentWinStreak(0);
        int totalGames = user.getTotalGames() + 1;
        user.setTotalGames(totalGames);
        int wins = user.getWins();
        int newRating = statService.calculateNewRatingAfterLoss(user.getRating());
        int lostRating = newRating - user.getRating();
        user.setRating(newRating);
        double winRate = statService.calculateWinRate(wins, totalGames);
        user.setWinRate(winRate);
        userRepository.save(user);
        return lostRating;
    }

    public boolean playSolveAction(User user, Duel duel, Long problemId){
        try{
            DuelPlayerStatus duelPlayerStatus = duelPlayerStatusRepository.updateProblemStatus(
                user.getId(), duel.getId(), problemId, DuelPlayerStatus.SolveStatus.SOLVED);
            Map<String, String> progress = duelPlayerStatus.getDuelProblemProgress();
            boolean is_player_win = progress.values().stream()
                .allMatch(status -> 
                    DuelPlayerStatus.SolveStatus.SOLVED.name().equals(status)
                );
            WsMessage solveMessage = WsMessage.builder()
                .type(WsMessage.MessageType.SOLVED)
                .sender(user.getNickname())
                .content("Игрок " + user.getNickname() + " решил задачу!")
                .build();
            if (user.getId() == duel.getConnectingUserId())
            {
                webSocketService.sendToUser(duel.getHostUserId(), solveMessage);
            } else{
                webSocketService.sendToUser(duel.getConnectingUserId(), solveMessage);
            }
            return is_player_win;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Не удалось обновить статус задачи: " + e.getMessage()
            );
        }

    }

    public void playAttemptAction(User user, Duel duel, Long problemId){
        try{
            DuelPlayerStatus duelPlayerStatus = duelPlayerStatusRepository.updateProblemStatus(
                user.getId(), duel.getId(), problemId, DuelPlayerStatus.SolveStatus.ATTEMPTED);
            Long hostId = duel.getHostUserId();
            Long opponentId = duel.getConnectingUserId();

            WsMessage attemptMessage = WsMessage.builder()
                .type(WsMessage.MessageType.ATTEMPT)
                .sender(user.getNickname())
                .content("Игрок " + user.getNickname() + " сделал попытку решения")
                .build();
            
            webSocketService.sendToUser(duel.getHostUserId(), attemptMessage);
            webSocketService.sendToUser(duel.getConnectingUserId(), attemptMessage);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Не удалось обновить статус задачи: " + e.getMessage()
            );
        }
    }
}
