package com.codagonki.app.DTO.Duel;

import com.codagonki.app.DTO.User.UserResponse;
import com.codagonki.app.models.DuelResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DuelResultResponse {
    private Long id;
    private Long duelId;
    private UserResponse opponent;
    private String resultType;
    private Integer gainedRating;
    private Integer enemyGainedRating;
    
    public DuelResultResponse(DuelResult duelResult) {
        this.id = duelResult.getId();
        this.duelId = duelResult.getDuel().getId();
        this.opponent = new UserResponse(duelResult.getOpponent());
        this.resultType = duelResult.getResultType().name();
        this.gainedRating = duelResult.getGainedRating();
        this.enemyGainedRating = duelResult.getEnemyGainedRating();
    }
}