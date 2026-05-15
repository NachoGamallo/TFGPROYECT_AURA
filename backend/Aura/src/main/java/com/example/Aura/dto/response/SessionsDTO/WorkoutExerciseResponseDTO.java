package com.example.Aura.dto.response.SessionsDTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WorkoutExerciseResponseDTO {

    private UUID exerciseId;
    private String exerciseName;
    private int position;
    private int restTime;
    private String exerciseImg;

    private List<WorkoutSetResponseDTO> sets;

}
