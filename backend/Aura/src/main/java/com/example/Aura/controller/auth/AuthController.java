package com.example.Aura.controller.auth;

import com.example.Aura.dto.request.LogInRequestDTO;
import com.example.Aura.dto.request.RegisterRequestDTO;
import com.example.Aura.dto.response.AuthResponseDTO;
import com.example.Aura.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInRequestDTO request){

        try {

            //Llamada al service para validar y obtener token.
            String token = authService.login(request);

            return ResponseEntity.ok(new AuthResponseDTO(token));

        }catch (RuntimeException e){

            //Si las credenciales fallan , devolvemos un 401 (Unauthorized)
            return ResponseEntity.status(401).body(e.getMessage());

        }catch (Exception e){

            return ResponseEntity.internalServerError().body("Error inesperado en el servidor");

        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request){

        try {
            String token = authService.register(request);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        }catch (Exception e){ return ResponseEntity.badRequest().body(e.getMessage()); }

    }
}
