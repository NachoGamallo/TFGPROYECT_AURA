package com.example.Aura.repository;

import com.example.Aura.model.ExerciseRecord;
import com.example.Aura.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, UUID> {

    List<ExerciseRecord> findBySession(TrainingSession session);

}
