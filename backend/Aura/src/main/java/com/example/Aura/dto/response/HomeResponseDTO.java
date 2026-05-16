package com.example.Aura.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class HomeResponseDTO {

    //Info principal card
    private int targetCalories;
    private int consumedCaloriesToday; //Quitar
    private int trainingMinutesToday;

    //Info cards
    private int completedWorkOuts;
    private int burnedCalories; //Quitar o adaptar
    private Double currentWeight;
    private int unlockedAchievements;

    private List<BodyRecordResponseDTO> bodyRecordInfoResponseDTOS;

}
