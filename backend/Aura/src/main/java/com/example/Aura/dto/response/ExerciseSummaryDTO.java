package com.example.Aura.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ExerciseSummaryDTO {

    private UUID id;
    private String name;
    private String imageURL;
    private String primaryMuscle; // Solo el principal para mostrar en la tarjeta

}
