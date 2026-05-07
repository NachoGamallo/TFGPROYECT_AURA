package com.example.Aura.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangeUserEmailRequestDTO {

    private UUID id;
    private String email;

}
