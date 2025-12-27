package com.codagonki.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")  
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column()
    private String nickname;
    
    private String role;
    
    @Column(name = "hashed_password")
    private String hashedPassword;  

    // Stats
    @Column(nullable = false)
    @Builder.Default
    private Integer rating = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxRating = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalGames = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer wins = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer losses = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer currentWinStreak = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer maxWinStreak = 0;

    @Column(nullable = false)
    @Builder.Default
    private Double winRate = 0.0;
}