package com.example.Aura.dto.response.SessionsDTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WorkoutSessionDataResponseDTO {

    private UUID routineId;
    private String routineName;
    private String routineDesc;

    //List of the exercises of the routine of this concret session.
    private List<WorkoutExerciseResponseDTO> exercises;

}
