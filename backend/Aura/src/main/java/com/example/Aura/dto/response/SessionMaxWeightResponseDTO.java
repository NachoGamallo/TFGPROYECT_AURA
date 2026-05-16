package com.example.Aura.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionMaxWeightResponseDTO {

    private LocalDateTime sessionDate;
    private Double maxWeight;

}
