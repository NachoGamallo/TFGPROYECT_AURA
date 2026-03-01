package com.example.Aura.services;

import com.example.Aura.model.Enums.ActivityLevel;
import com.example.Aura.model.Enums.Gender;
import com.example.Aura.model.NutritionPlan;
import com.example.Aura.model.PhysicalProfile;
import org.springframework.stereotype.Service;

@Service
public class NutritionService {

    public NutritionPlan calculateAndSaveInitialPlan(PhysicalProfile profile){

        //Calcular el metabolismo Basal.
        double bmr;
        if (profile.getGender() == Gender.MALE){

            bmr = (10 * profile.getInitialWeight()) + (6.25 * profile.getHeightCM()) - (5 * profile.getAge()) + 5;

        }else {

            bmr = (10 * profile.getInitialWeight()) + (6.25 * profile.getHeightCM()) - (5 * profile.getAge()) - 161;

        }

        //Multiplicar por nivel de actividad.
        double activityMultiplier = getActivityMultiplier(profile.getActivityLevel());
        double tdee = bmr * activityMultiplier;

        //Ajustar calorias segun objetivo.
        double targetCalories = tdee;
        switch (profile.getGoal()) {
            case FAT_LOSS: // o CUTTING
                targetCalories -= 500; // Déficit de 500 kcal
                break;
            case MUSCLE_GAIN: // o BULKING
                targetCalories += 300; // Superávit de 300 kcal
                break;
            case MAINTENANCE: // o MAINTENANCE
                // Se mantiene el TDEE
                break;
        }

        //Calcular Macros.
        double proteinGrams = profile.getInitialWeight() * 2.2;
        double fatGrams = profile.getInitialWeight() * 1.0;

        // Las proteínas y carbos tienen 4 kcal/g, las grasas 9 kcal/g
        double caloriesFromProteinAndFat = (proteinGrams * 4) + (fatGrams * 9);
        double remainingCalories = targetCalories - caloriesFromProteinAndFat;
        double carbGrams = remainingCalories / 4;

        NutritionPlan plan = new NutritionPlan();
        plan.setAppUser(profile.getAppUser());
        plan.setCalories((int) targetCalories);
        plan.setProtein(proteinGrams);
        plan.setFats(fatGrams);
        plan.setCarbs(carbGrams);

        // planRepo.save(plan);
        return plan;

    }


    private Double getActivityMultiplier(ActivityLevel level){

        return switch (level){

            case SEDENTARY -> 1.2;
            case LIGHT -> 1.375;
            case MODERATE -> 1.55;
            case HIGH -> 1.725;
            case ATHLETE -> 1.9;

        };

    }

}
