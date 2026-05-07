package com.example.Aura.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDataDTO {

    private UUID id;
    private String email;
    private String name;
    private String imageURL;

}
