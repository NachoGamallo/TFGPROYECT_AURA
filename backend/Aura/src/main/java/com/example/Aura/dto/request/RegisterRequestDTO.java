package com.example.Aura.dto.request;

import com.example.Aura.model.Enums.*;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    //Pantalla 1.
    private String email;
    private String password;
    private String name;

    //Pantalla 2.
    private Gender gender;
    private Double initialWeight;
    private int heightCM;
    private int age;

    //Pantalla 3.
    private Goal goal;
    private ActivityLevel activityLevel;
    private UserLevel level;

}
