package com.example.Aura.repository;

import com.example.Aura.model.AppUser;
import com.example.Aura.model.Routine;
import com.example.Aura.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, UUID> {

    Optional<TrainingSession> findTopByUserAndRoutineOrderByCreatedAtDesc(AppUser user, Routine routine);

    long countByUserAndCreatedAtBetween(AppUser user, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    // Consulta para sumar la duración de los entrenamientos del usuario entre dos fechas/horas
    @Query("SELECT SUM(ts.durationMinutes) FROM TrainingSession ts " +
            "WHERE ts.user = :user AND ts.createdAt >= :startOfDay AND ts.createdAt <= :endOfDay")
    Integer sumDurationMinutesByUserAndCreatedAtBetween(
            @Param("user") AppUser user,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

}
