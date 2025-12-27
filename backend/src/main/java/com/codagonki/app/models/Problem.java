package com.codagonki.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.codagonki.app.DTO.Problem.ExampleResponse;
import com.codagonki.app.DTO.Problem.HintResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "problems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    // @Column(name = "title_slug", nullable = false, unique = true)
    // private String titleSlug; 

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ExampleResponse> examples;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty; // EASY, MEDIUM, HARD

    @Column(name = "func_name")
    @Builder.Default
    private String funcName = "execute";
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_data_types", columnDefinition = "jsonb")
    private Map<String, Object> inputDataTypes;

    @Column(name = "output_data_type")
    private String outputDataType;

    // @Column(name = "time_limit_ms", nullable = false)
    // private Integer timeLimitMs; 

    // @Column(name = "memory_limit_mb", nullable = false)
    // private Integer memoryLimitMb; 

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "hints", columnDefinition = "jsonb")
    private List<HintResponse> hints;

    @ManyToMany(mappedBy = "problems")
    @Builder.Default
    @JsonIgnore
    private List<Duel> duels = new ArrayList<>();

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}