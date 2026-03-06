package com.example.Aura.model;

import com.example.Aura.model.ComposesID.ExerciseMuscleID;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "exercise_muscle")
public class ExerciseMuscle {

    @EmbeddedId
    private ExerciseMuscleID id;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @MapsId("muscleGroupId")
    @JoinColumn(name = "muscle_group_id")
    private MuscleGroup muscleGroup;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

}
