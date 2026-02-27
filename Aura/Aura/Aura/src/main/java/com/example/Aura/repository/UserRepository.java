package com.example.Aura.repository;

import com.example.Aura.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findAppUserByEmail(String email);

    boolean existsAppUserByEmail(String email);

}
