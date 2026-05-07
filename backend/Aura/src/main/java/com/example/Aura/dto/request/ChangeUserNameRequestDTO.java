package com.example.Aura.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangeUserNameRequestDTO {

    private UUID id;
    private String name;

}
