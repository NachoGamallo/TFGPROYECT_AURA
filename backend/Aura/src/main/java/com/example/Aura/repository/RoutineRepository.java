package com.example.Aura.repository;

import com.example.Aura.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoutineRepository extends JpaRepository<Routine, UUID> {



}
