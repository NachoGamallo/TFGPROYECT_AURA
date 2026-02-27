package com.example.Aura.services;

import com.example.Aura.dto.request.LogInRequest;
import com.example.Aura.model.AppUser;
import com.example.Aura.repository.UserRepository;
import com.example.Aura.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public String login(LogInRequest request){

        // 1. Buscamos usuario por email.
        AppUser user = userRepo.findAppUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //2. Comprobamos la clave del JSON con el password_hash de la BBDD.
        // Importante: passwordEncoder.matches entiende que el hash de tu SQL es BCrypt.
        if (!passwordEncoder.matches(request.getPass(), user.getPassword())) {

            throw new RuntimeException("Credenciales incorrectas");

        }

        //3. Si es correcto generamos el token.
        return jwtService.generateToken(user.getEmail());

    }

}
