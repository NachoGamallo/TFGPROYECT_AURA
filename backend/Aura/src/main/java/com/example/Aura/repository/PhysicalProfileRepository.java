package com.example.Aura.repository;

import com.example.Aura.model.AppUser;
import com.example.Aura.model.PhysicalProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PhysicalProfileRepository extends JpaRepository <PhysicalProfile, UUID> {
    Optional<PhysicalProfile> findByAppUser(AppUser appUser);
}
