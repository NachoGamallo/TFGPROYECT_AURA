package com.example.Aura.dto.request.SessionsDTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SaveExercisesRecordRequestDTO {

    private UUID exerciseId;
    private List<SaveSetRecordRequestDTO> sets;

}
