package com.example.Aura.dto.response.SessionsDTO;

import lombok.Data;

@Data
public class WorkoutSetResponseDTO {

    private int id;
    private int setNumber;
    private double previousWeight;
    private int previousReps;

}
