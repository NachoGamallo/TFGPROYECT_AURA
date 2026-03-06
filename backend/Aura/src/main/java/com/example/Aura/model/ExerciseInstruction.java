package com.example.Aura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "exercise_instruction")
public class ExerciseInstruction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "exercise_id", unique = true)
    private Exercise exercise;

    @Column(nullable = false, unique = true, name = "step_number")
    private Integer stepNumber;

    @Column(name = "instruction_text", nullable = false)
    private String text;

}
