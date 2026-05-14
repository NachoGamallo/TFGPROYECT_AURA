package com.example.Aura.dto.request.SessionsDTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SaveTrainingSessionRequestDTO {

    private UUID routineId;
    private Long durationSeconds;
    List<SaveExercisesRecordRequestDTO> exercises;

}
