package com.example.Aura.repository;

import com.example.Aura.dto.response.SessionMaxWeightResponseDTO;
import com.example.Aura.model.ExerciseRecord;
import com.example.Aura.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, UUID> {

    List<ExerciseRecord> findBySession(TrainingSession session);

    // 1. Obtener el peso máximo histórico de un usuario para un ejercicio.
    @Query("SELECT MAX(er.weight) FROM ExerciseRecord er WHERE er.exercise.id = :exerciseId AND er.session.user.id = :userId")
    Double findHistoricalMaxWeight(@Param("exerciseId") UUID exerciseId, @Param("userId") UUID userId);

    // 2. Obtener el peso máximo por sesión/día para un ejercicio.
    @Query("SELECT new com.example.Aura.dto.response.SessionMaxWeightResponseDTO(CAST(er.session.createdAt AS localdatetime), MAX(er.weight)) " +
            "FROM ExerciseRecord er " +
            "WHERE er.exercise.id = :exerciseId AND er.session.user.id = :userId " +
            "GROUP BY CAST(er.session.createdAt AS localdatetime) " +
            "ORDER BY CAST(er.session.createdAt AS localdatetime) ASC")
    List<SessionMaxWeightResponseDTO> findMaxWeightPerSession(@Param("exerciseId") UUID exerciseId, @Param("userId") UUID userId);

}
