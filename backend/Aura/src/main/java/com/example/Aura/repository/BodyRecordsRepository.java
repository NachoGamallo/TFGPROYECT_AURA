package com.example.Aura.repository;

import com.example.Aura.model.BodyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BodyRecordsRepository extends JpaRepository<BodyRecord, UUID> {
    
    List<BodyRecord> findByUserIdOrderByCreatedAtAsc(UUID userId);
    
}
