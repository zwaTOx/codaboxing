package com.codagonki.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "duel_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuelResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opponent_id", nullable = false)
    private User opponent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duel_id", nullable = false)
    private Duel duel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DuelResultType resultType;

    @Column(name = "gained_rating", nullable = false)
    private Integer gainedRating;

    @Column(name = "enemy_gained_rating", nullable = false)
    private Integer enemyGainedRating;
    
    public enum DuelResultType {
        WIN, LOSS
    }
}