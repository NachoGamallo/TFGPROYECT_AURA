package com.example.Aura.dto.request;

import lombok.Data;

@Data
public class ChangeUserPasswordRequestDTO {

    private String oldPassword;
    private String newPassword;

}
