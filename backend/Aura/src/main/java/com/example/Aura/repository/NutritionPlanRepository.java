package com.example.Aura.repository;

import com.example.Aura.model.AppUser;
import com.example.Aura.model.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NutritionPlanRepository extends JpaRepository <NutritionPlan , UUID> {
    Optional<NutritionPlan> findByAppUserAndIsActiveTrue(AppUser appUser);
}
