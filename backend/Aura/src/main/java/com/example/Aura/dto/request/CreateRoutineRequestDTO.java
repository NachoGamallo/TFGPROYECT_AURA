package com.example.Aura.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoutineRequestDTO {

    private String name;
    private String desc;
    private List<RoutineExerciseRequestDTO> exercises;

}
