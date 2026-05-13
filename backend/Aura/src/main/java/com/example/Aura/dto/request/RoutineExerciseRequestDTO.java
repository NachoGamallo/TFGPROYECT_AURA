package com.example.Aura.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RoutineExerciseRequestDTO {

    private UUID exerciseId;
    private int position;
    private int sets;
    private int reps;
    private int restSeconds;
    private Double initialWeight;

}
