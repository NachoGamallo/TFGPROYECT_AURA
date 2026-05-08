package com.example.Aura.controller.auth;

import com.example.Aura.dto.request.ChangeUserEmailRequestDTO;
import com.example.Aura.dto.request.ChangeUserImageRequestDTO;
import com.example.Aura.dto.request.ChangeUserNameRequestDTO;
import com.example.Aura.dto.request.ChangeUserPasswordRequestDTO;
import com.example.Aura.dto.response.HomeResponseDTO;
import com.example.Aura.dto.response.UserDataDTO;
import com.example.Aura.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/data")
    public ResponseEntity<UserDataDTO> getUserData(Authentication authentication){

        String userEmail = authentication.getName();
        return ResponseEntity.ok(userService.getUserData(userEmail));

    }

    // 07-05 - 6:19 Update name,email and password of the user , controller. By Nacho
    @PatchMapping("/change-name")
    public ResponseEntity<String> changeName(@RequestBody ChangeUserNameRequestDTO requestDTO){
        userService.updateName(requestDTO);
        return ResponseEntity.ok("Nombre actualizado correctamente");
    }

    @PatchMapping("/change-email")
    public ResponseEntity<String> changeEmail(@RequestBody ChangeUserEmailRequestDTO requestDTO){
        userService.updateEmail(requestDTO);
        return ResponseEntity.ok("Email actualizado correctamente");
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangeUserPasswordRequestDTO requestDTO){
        userService.updatePassword(requestDTO);
        return ResponseEntity.ok("Contraseña actualizado correctamente");
    }
    //End update of the 07-05

    //Controller to change user image 08/05 15:52
    @PatchMapping("/change-img")
    public ResponseEntity<String> changeImg(@RequestBody ChangeUserImageRequestDTO requestDTO){
        userService.updateUserImage(requestDTO);
        return ResponseEntity.ok("Imagen actualizada correctamente");
    }

    //Controller to delete the user
    @PatchMapping("/delete")
    public ResponseEntity<String> deleteUser(){
        userService.deleteUser();
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

}
