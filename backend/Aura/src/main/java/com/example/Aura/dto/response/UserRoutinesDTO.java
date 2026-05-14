package com.example.Aura.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserRoutinesDTO {

    private UUID id;
    private String name;
    private String desc;

}
