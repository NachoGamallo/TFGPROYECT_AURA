package com.example.Aura.dto.response;

import com.example.Aura.model.Enums.ExerciseType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ExerciseDetailsDTO {

    private UUID id;
    private String name;
    private String desc;
    private ExerciseType exerciseType;
    private String equipment;
    private String imgURL;
    private List<String> primaryMuscles; //Nombre de los primarios
    private List<String> secondaryMuscles; //Nombre de los secundarios
    private List<String> instructions; //Texto de instrucciones

}
