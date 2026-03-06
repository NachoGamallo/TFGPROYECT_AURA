package com.example.Aura.model;

import com.example.Aura.model.Enums.ExerciseType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table (name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "exercise_type" , nullable = false)
    private ExerciseType exerciseType;

    private String equipment;

    @Column(name = "image_url")
    private String imageURL;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "exercice" , cascade = CascadeType.ALL)
    private List<ExerciseMuscle> exerciseMuscles;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<ExerciseInstruction> instructions;

}
