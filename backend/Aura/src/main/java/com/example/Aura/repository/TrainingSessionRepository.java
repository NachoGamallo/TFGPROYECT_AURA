package com.example.Aura.repository;

import com.example.Aura.model.AppUser;
import com.example.Aura.model.Routine;
import com.example.Aura.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, UUID> {

    Optional<TrainingSession> findTopByUserAndRoutineOrderByCreatedAtDesc(AppUser user, Routine routine);

}
