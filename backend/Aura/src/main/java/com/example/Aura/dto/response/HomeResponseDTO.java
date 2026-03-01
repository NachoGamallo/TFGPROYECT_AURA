package com.example.Aura.dto.response;

import lombok.Data;

@Data
public class HomeResponseDTO {

    private String userName;

    //Info principal card
    private int targetCalories;
    private int consumedCaloriesToday;
    private int trainingMinutesToday;

    //Info cards
    private int completedWorkOuts;
    private int burnedCalories;
    private Double currentWeight;
    private int unlockedAchievements;

}
