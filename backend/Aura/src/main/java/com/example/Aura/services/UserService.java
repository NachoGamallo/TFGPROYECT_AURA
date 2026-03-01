package com.example.Aura.services;

import com.example.Aura.dto.response.HomeResponseDTO;
import com.example.Aura.model.AppUser;
import com.example.Aura.model.NutritionPlan;
import com.example.Aura.model.PhysicalProfile;
import com.example.Aura.repository.NutritionPlanRepository;
import com.example.Aura.repository.PhysicalProfileRepository;
import com.example.Aura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PhysicalProfileRepository physicalProfileRepo;

    @Autowired
    private NutritionPlanRepository nutritionPlanRepo;

    public HomeResponseDTO getHomeData(String email){

        //Buscamos el usuario y sus datos.
        AppUser user = userRepo.findAppUserByEmail(email).orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        PhysicalProfile profile = physicalProfileRepo.findByAppUser(user).orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        NutritionPlan plan = nutritionPlanRepo.findByAppUserAndIsActiveTrue(user).orElseThrow(() -> new RuntimeException("Plan nutricional no encontrado"));

        //Mapear el DTO.
        HomeResponseDTO response = new HomeResponseDTO();

        //Datos reales.
        response.setUserName(user.getName());
        response.setTargetCalories(plan.getCalories());
        response.setCurrentWeight(profile.getInitialWeight());

        //Datos temporales (Aun no esta implementado porque no tenemos los modulos de Entreno/Dieta).
        // TODO: Hacer queries a FoodRecordRepo, TrainingSessionRepo, etc.

        response.setConsumedCaloriesToday(0);
        response.setTrainingMinutesToday(0);
        response.setCompletedWorkOuts(0);
        response.setBurnedCalories(0);
        response.setUnlockedAchievements(0);

        return response;

    }

}
