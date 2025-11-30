package com.codagonki.app.repositories;

import com.codagonki.app.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findByTitle(String title);
    List<Problem> findByDifficulty(Problem.Difficulty difficulty);
    boolean existsById(Long id);

    @Query(value = "SELECT COUNT(*) FROM duel_problems WHERE duel_id = :duelId", 
        nativeQuery = true)
    int getProblemCount(@Param("duelId") Long duelId);

    @Query("SELECT p FROM Problem p JOIN p.duels d WHERE d.id = :id")
    List<Problem> findByDuelId(@Param("id") Long id);

    @Query(value = """
        SELECT * FROM problems 
        WHERE id IN (
            SELECT id FROM problems 
            WHERE difficulty = COALESCE(:difficulty, difficulty)
            ORDER BY RANDOM() 
            LIMIT :count
        )
        """, nativeQuery = true)
    List<Problem> findRandomProblemsWithDifficulty(
        @Param("count") int count,
        @Param("difficulty") String difficulty
    );

    @Query(value = "SELECT * FROM problems ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Problem> findRandomProblems(@Param("count") int count);
}