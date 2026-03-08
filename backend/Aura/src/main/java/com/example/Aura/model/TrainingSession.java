package com.example.Aura.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "training_session")
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @CreationTimestamp
    @Column(name = "session_date", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "duration_minutes")
    private int durationMinutes = 0;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
