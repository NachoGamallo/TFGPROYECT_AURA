package com.example.Aura.repository;

import com.example.Aura.model.RoutineExercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercice, UUID> {
}
