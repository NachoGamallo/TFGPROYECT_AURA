package com.example.Aura.model;

import com.example.Aura.model.Enums.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "physical_profile")
public class PhysicalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private AppUser appUser;

    @Column(nullable = false, name = "age")
    private int age;

    @Column(nullable = false, name = "height_cm")
    private int heightCM;

    @Column(nullable = false, name = "initial_weight")
    private Double initialWeight;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "gender")
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "level")
    private UserLevel level = UserLevel.BEGINNER;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "activity_level")
    private ActivityLevel activityLevel = ActivityLevel.MODERATE;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false , name = "goal")
    private Goal goal;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "phase")
    private UserPhase phase;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
