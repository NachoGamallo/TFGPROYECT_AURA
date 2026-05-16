package com.example.Aura.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseReportResponseDTO {

    private Double historicalMaxWeight;
    private List<SessionMaxWeightResponseDTO> sessionRecords;

}
