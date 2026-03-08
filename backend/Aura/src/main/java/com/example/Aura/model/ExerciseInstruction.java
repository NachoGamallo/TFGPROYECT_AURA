package com.example.Aura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "exercise_instruction",
        uniqueConstraints = @UniqueConstraint(columnNames = {"exercise_id", "step_number"}))
public class ExerciseInstruction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(nullable = false, name = "step_number")
    private Integer stepNumber;

    @Column(name = "instruction_text", nullable = false)
    private String text;

}
