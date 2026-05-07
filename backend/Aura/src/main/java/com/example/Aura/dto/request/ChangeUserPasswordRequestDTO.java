package com.example.Aura.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangeUserPasswordRequestDTO {

    private UUID id;
    private String oldPassword;
    private String newPassword;

}
