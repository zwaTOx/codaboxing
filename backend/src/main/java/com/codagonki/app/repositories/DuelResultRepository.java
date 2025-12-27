package com.codagonki.app.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.codagonki.app.models.DuelResult;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface DuelResultRepository extends JpaRepository<DuelResult, Long> {
    Page<DuelResult> findByUserId(Long userId, Pageable pageable);

        @Query("SELECT dr FROM DuelResult dr " +
           "JOIN FETCH dr.user " +
           "JOIN FETCH dr.opponent " +
           "JOIN FETCH dr.duel " +
           "WHERE dr.user.id = :userId")
    List<DuelResult> findByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT dr FROM DuelResult dr JOIN FETCH dr.opponent WHERE dr.user.id = :userId")
    List<DuelResult> findByUserId(@Param("userId") Long userId);
}
