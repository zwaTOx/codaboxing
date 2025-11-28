package com.codagonki.app.DTO.User;

import com.codagonki.app.models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String email;
    private String nickname;
    private String role;
    private Integer rating;
    private Integer maxRating;
    private Integer totalGames;
    private Integer wins;
    private Integer losses;
    private Integer currentWinStreak;
    private Integer maxWinStreak;
    private Double winRate;

    public UserProfileResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.rating = user.getRating();
        this.maxRating = user.getMaxRating();
        this.totalGames = user.getTotalGames();
        this.wins = user.getWins();
        this.losses = user.getLosses();
        this.currentWinStreak = user.getCurrentWinStreak();
        this.maxWinStreak = user.getMaxWinStreak();
        this.winRate = user.getWinRate();
    }
}