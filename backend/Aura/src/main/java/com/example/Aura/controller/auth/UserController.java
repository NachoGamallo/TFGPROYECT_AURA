package com.example.Aura.controller.auth;

import com.example.Aura.dto.response.HomeResponseDTO;
import com.example.Aura.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public ResponseEntity<HomeResponseDTO> getHomeData(Authentication authentication){

        String userEmail = authentication.getName();
        return ResponseEntity.ok(userService.getHomeData(userEmail));

    }

}
