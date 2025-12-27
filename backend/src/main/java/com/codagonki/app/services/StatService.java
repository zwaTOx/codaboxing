package com.codagonki.app.services;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class StatService {
    public int calculateNewRatingAfterWin() {
        Random random = new Random();
        int baseGain = 15;
        int randomDeviation = random.nextInt(7) - 3;
        int totalChange = Math.max(12, Math.min(18, baseGain + randomDeviation));
        return totalChange;
    }
    
    public int calculateNewRatingAfterLoss(int currentRating) {
        Random random = new Random();
        int baseLoss = 12;
        int randomDeviation = random.nextInt(7) - 3;
        int totalChange = Math.max(7, Math.min(13, baseLoss + randomDeviation));
        return Math.max(0, currentRating - totalChange);
    }

    public double calculateWinRate(int wins, int totalGames) {
        if (totalGames == 0) {
            return 0.0;
        }
        double winRate = (double) wins / totalGames * 100.0;
        winRate = Math.round(winRate * 100.0) / 100.0;
        return Math.max(0.0, Math.min(100.0, winRate));
    }
}
