package com.example.Aura.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BodyRecordResponseDTO {

    private LocalDateTime recordDate;
    private Double weight;

}
