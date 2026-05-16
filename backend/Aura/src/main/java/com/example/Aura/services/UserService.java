package com.example.Aura.services;

import com.example.Aura.dto.request.ChangeUserEmailRequestDTO;
import com.example.Aura.dto.request.ChangeUserImageRequestDTO;
import com.example.Aura.dto.request.ChangeUserNameRequestDTO;
import com.example.Aura.dto.request.ChangeUserPasswordRequestDTO;
import com.example.Aura.dto.response.HomeResponseDTO;
import com.example.Aura.dto.response.SessionMaxWeightResponseDTO;
import com.example.Aura.dto.response.UserDataDTO;
import com.example.Aura.model.AppUser;
import com.example.Aura.model.BodyRecord;
import com.example.Aura.model.NutritionPlan;
import com.example.Aura.model.PhysicalProfile;
import com.example.Aura.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class UserService {

    @Autowired private UserRepository userRepo;
    @Autowired private PhysicalProfileRepository physicalProfileRepo;
    @Autowired private NutritionPlanRepository nutritionPlanRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private BodyRecordsRepository bodyRecordRepo;
    @Autowired private TrainingSessionRepository trainingSessionRepo;

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

        List<BodyRecord>bodyRecords = bodyRecordRepo.findByUserIdOrderByCreatedAtAsc(user.getId());

        //Tiempo de entreno hoy.
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Integer totalMinutesToday = trainingSessionRepo.sumDurationMinutesByUserAndCreatedAtBetween(user,startOfDay,endOfDay);
        response.setTrainingMinutesToday(totalMinutesToday != null ? totalMinutesToday : 0);

        //Numero de entrenos de la semana.
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);

        long workoutsThisWeek = trainingSessionRepo.countByUserAndCreatedAtBetween(user,startOfWeek,endOfWeek);
        response.setCompletedWorkOuts(Math.toIntExact(workoutsThisWeek));

        if (!bodyRecords.isEmpty()){

            response.setCurrentWeight(bodyRecords.get(bodyRecords.size() - 1).getWeight());

        } else {

            response.setCurrentWeight(profile.getInitialWeight());

        }

        List<SessionMaxWeightResponseDTO> historyDTO = bodyRecords.stream().map(record -> {

            SessionMaxWeightResponseDTO dto = new SessionMaxWeightResponseDTO();
            dto.setSessionDate(record.getCreatedAt());
            dto.setMaxWeight(record.getWeight());
            return dto;

        }).toList();

        response.setBodyRecordInfoResponseDTOS(historyDTO);

        response.setConsumedCaloriesToday(0); //No lo haremos por ahora
        response.setTrainingMinutesToday(0);
        response.setBurnedCalories(0); // No lo hare por ahora
        response.setUnlockedAchievements(0); //No lo hare por ahora

        return response;

    }

    public UserDataDTO getUserData(String email){

        //Buscamos el usuario y sus datos.
        AppUser user = userRepo.findAppUserByEmail(email).orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        UserDataDTO response = new UserDataDTO();

        //Datos que pasaremos.
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setImageURL(user.getImage());

        return response;

    }

    //Added the 07-05 6:22 - Validation structure and update of user name,email and password.
    public AppUser getAuthenticatedUser(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findAppUserByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    }

    public void updateName(ChangeUserNameRequestDTO requestDTO){

        AppUser user = getAuthenticatedUser();

        //1. We change the name.
        user.setName(requestDTO.getName());
        userRepo.save(user);

    }

    public void updateEmail(ChangeUserEmailRequestDTO requestDTO){

        AppUser user = getAuthenticatedUser();

        //1.We check that the email is not used by other user
        if (userRepo.existsAppUserByEmail(requestDTO.getEmail())) throw new RuntimeException("El email ya esta registrado");

        //2.We change the email.
        user.setEmail(requestDTO.getEmail());
        userRepo.save(user);

    }

    public void updatePassword(ChangeUserPasswordRequestDTO requestDTO){

        AppUser user = getAuthenticatedUser();

        //1.we verify that the older password is the same.
        if (!passwordEncoder.matches(requestDTO.getOldPassword(), user.getPassword())) throw new RuntimeException("La contraseña actual es incorrecta");

        //2.We change the password.
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userRepo.save(user);

    }

    //Change the user profile Image - 08/05/2026 - 15:41.
    public void updateUserImage(ChangeUserImageRequestDTO requestDTO){

        AppUser user = getAuthenticatedUser();

        //1.We change the image of the user.
        user.setImage(requestDTO.getImg());
        userRepo.save(user);

    }

    //Delete the user - 08/05/2026 15:49.
    public void deleteUser(){

        AppUser user = getAuthenticatedUser();

        //1. We mark the user as delete (deleted_at)
        user.setDeletedAt(LocalDateTime.now());
        userRepo.save(user);

    }

}
