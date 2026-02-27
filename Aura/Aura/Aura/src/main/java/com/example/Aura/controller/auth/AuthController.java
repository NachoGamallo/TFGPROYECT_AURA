package com.example.Aura.controller.auth;

import com.example.Aura.dto.request.LogInRequest;
import com.example.Aura.dto.response.AuthResponse;
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
    public ResponseEntity<?> login(@RequestBody LogInRequest request){

        try {

            //Llamada al service para validar y obtener token.
            String token = authService.login(request);

            return ResponseEntity.ok(new AuthResponse(token));

        }catch (RuntimeException e){

            //Si las credenciales fallan , devolvemos un 401 (Unauthorized)
            return ResponseEntity.status(401).body(e.getMessage());

        }catch (Exception e){

            return ResponseEntity.internalServerError().body("Error inesperado en el servidor");

        }

    }

}
