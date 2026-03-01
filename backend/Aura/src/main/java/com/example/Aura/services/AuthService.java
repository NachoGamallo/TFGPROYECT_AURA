package com.example.Aura.services;

import com.example.Aura.dto.request.LogInRequestDTO;
import com.example.Aura.dto.request.RegisterRequestDTO;
import com.example.Aura.model.AppUser;
import com.example.Aura.model.Enums.UserPhase;
import com.example.Aura.model.NutritionPlan;
import com.example.Aura.model.PhysicalProfile;
import com.example.Aura.repository.NutritionPlanRepository;
import com.example.Aura.repository.PhysicalProfileRepository;
import com.example.Aura.repository.UserRepository;
import com.example.Aura.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    PhysicalProfileRepository profileRepo;

    @Autowired
    NutritionService nutritionService;

    @Autowired
    NutritionPlanRepository nutritionPlanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public String login(LogInRequestDTO request){

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

    @Transactional //O se guardan ambas tablas o ninguna.
    public String register(RegisterRequestDTO request){

        if (userRepo.existsAppUserByEmail(request.getEmail())) throw new RuntimeException("El email ya esta registrado");

        //Creamos instancia de usuario y mapeamos datos.
        AppUser newUser = new AppUser();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        AppUser savedUser = userRepo.save(newUser);

        //Creamos y generamos physicalProfile referenciado al usuario.
        PhysicalProfile profile = new PhysicalProfile();
        profile.setAppUser(savedUser); //Usuario generado arriba.
        profile.setGender(request.getGender());
        profile.setInitialWeight(request.getInitialWeight());
        profile.setHeightCM(request.getHeightCM());
        profile.setAge(request.getAge());
        profile.setGoal(request.getGoal());
        profile.setActivityLevel(request.getActivityLevel());
        profile.setLevel(request.getLevel());

        switch (request.getGoal()){
            case FAT_LOSS -> profile.setPhase(UserPhase.CUTTING);
            case MUSCLE_GAIN -> profile.setPhase(UserPhase.BULKING);
            case MAINTENANCE -> profile.setPhase(UserPhase.MAINTENANCE);
        }

        profileRepo.save(profile);

        NutritionPlan nutritionPlan = nutritionService.calculateAndSaveInitialPlan(profile);
        nutritionPlanRepository.save(nutritionPlan);

        return jwtService.generateToken(savedUser.getEmail());

    }

}
