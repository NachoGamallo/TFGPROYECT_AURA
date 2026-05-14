package com.example.Aura.repository;

import com.example.Aura.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoutineRepository extends JpaRepository<Routine, UUID> {

        List<Routine> getRoutinesByCreator_Id(UUID creatorId);

}
