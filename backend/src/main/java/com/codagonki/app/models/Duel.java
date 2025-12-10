package com.codagonki.app.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "duels")  
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Duel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  
    
    @Column(name = "host_user_id", nullable = false)
    private Long hostUserId;
    
    @Column(name = "connecting_user_id")
    private Long connectingUserId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DuelStatus status;
    
    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "problem_count")
    private Integer problemCount;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "duel_problems",
        joinColumns = @JoinColumn(name = "duel_id"),
        inverseJoinColumns = @JoinColumn(name = "problem_id")
    )
    @Builder.Default
    private List<Problem> problems = new ArrayList<>();

    public enum DuelStatus {
        WAITING,    
        ACTIVE,     
        COMPLETED,  
        CANCELLED   
    }
}