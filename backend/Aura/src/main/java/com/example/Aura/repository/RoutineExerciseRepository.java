package com.example.Aura.repository;

import com.example.Aura.model.Routine;
import com.example.Aura.model.RoutineExercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercice, UUID> {

    List<RoutineExercice> findByRoutineOrderByPositionAsc(Routine routine);

}
