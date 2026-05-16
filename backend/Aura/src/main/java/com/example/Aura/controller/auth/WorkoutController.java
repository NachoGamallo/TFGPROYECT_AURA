package com.example.Aura.controller.auth;

import com.example.Aura.dto.request.SessionsDTO.SaveTrainingSessionRequestDTO;
import com.example.Aura.dto.response.SessionsDTO.WorkoutSessionDataResponseDTO;
import com.example.Aura.services.TrainingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/workout")
@CrossOrigin("*")
public class WorkoutController {

    @Autowired TrainingSessionService trainingSessionService;

    @GetMapping("/start/{routineId}")
    public ResponseEntity<WorkoutSessionDataResponseDTO> startWorkout(@PathVariable UUID routineId){

       try{

           WorkoutSessionDataResponseDTO workoutData = trainingSessionService.getWorkoutDataForRoutine(routineId);
           return ResponseEntity.ok(workoutData);

       }catch (Exception e){

            return ResponseEntity.badRequest().build();

       }

    }

    @PostMapping("/finish")
    public ResponseEntity<String> finishWorkout(@RequestBody SaveTrainingSessionRequestDTO requestDTO){

        trainingSessionService.saveCompletedSession(requestDTO);
        return ResponseEntity.ok("Training session saved");

    }

}
