package com.example.Aura.model;

import com.example.Aura.model.ComposesID.RoutineExerciceID;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "routine_exercise")
public class RoutineExercice {

    @EmbeddedId
    private RoutineExerciceID id;

    @ManyToOne
    @MapsId("routineId")
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "sets")
    private int sets = 3;

    @Column(name = "repetitions")
    private int reps = 10;

    @Column(name = "rest_seconds")
    private int rest = 60;

}
