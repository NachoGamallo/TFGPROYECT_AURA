package com.example.Aura.services;

import com.example.Aura.dto.request.SessionsDTO.SaveExercisesRecordRequestDTO;
import com.example.Aura.dto.request.SessionsDTO.SaveSetRecordRequestDTO;
import com.example.Aura.dto.request.SessionsDTO.SaveTrainingSessionRequestDTO;
import com.example.Aura.dto.response.SessionsDTO.WorkoutExerciseResponseDTO;
import com.example.Aura.dto.response.SessionsDTO.WorkoutSessionDataResponseDTO;
import com.example.Aura.dto.response.SessionsDTO.WorkoutSetResponseDTO;
import com.example.Aura.model.*;
import com.example.Aura.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingSessionService {

    @Autowired UserService userService;
    @Autowired RoutineRepository routineRepo;
    @Autowired RoutineExerciseRepository routineExerciseRepo;
    @Autowired TrainingSessionRepository trainingSessionRepo;
    @Autowired ExerciseRecordRepository exerciseRecordRepo;
    @Autowired ExerciseRepository exerciseRepo;

    //Get initial data
    public WorkoutSessionDataResponseDTO getWorkoutDataForRoutine(UUID routineId){

        AppUser user = userService.getAuthenticatedUser();
        Routine routine = routineRepo.findById(routineId).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        WorkoutSessionDataResponseDTO responseData = new WorkoutSessionDataResponseDTO();
        responseData.setRoutineId(routine.getId());
        responseData.setRoutineName(routine.getName());
        responseData.setRoutineDesc(routine.getDescription());

        //1.We are gonna search if there is a lastest sessión.
        TrainingSession lastSession = trainingSessionRepo.findTopByUserAndRoutineOrderByCreatedAtDesc(user,routine).orElse(null);
        List<ExerciseRecord> lastSessionRecord = new ArrayList<>();
        if (lastSession != null){

            lastSessionRecord = exerciseRecordRepo.findBySession(lastSession);

        }

        //2.Get base data of the routine.
        List<RoutineExercice> routineExercices = routineExerciseRepo.findByRoutineOrderByPositionAsc(routine);
        List<WorkoutExerciseResponseDTO> exercisesDTO = new ArrayList<>();

        for (RoutineExercice re : routineExercices){

            WorkoutExerciseResponseDTO exDTO = new WorkoutExerciseResponseDTO();
            exDTO.setExerciseId(re.getExercise().getId());
            exDTO.setExerciseImg(re.getExercise().getImageURL());
            exDTO.setExerciseName(re.getExercise().getName());
            exDTO.setPosition(re.getPosition());
            exDTO.setRestTime(re.getRestSeconds());

            List<WorkoutSetResponseDTO> setDTOs = new ArrayList<>();

            //We generate the number of series that we have in the data.
            for (int i = 1 ; i <= re.getSets() ; i ++){

                WorkoutSetResponseDTO setDTO = new WorkoutSetResponseDTO();
                setDTO.setSetNumber(i);

                //We search if in the last sesion it made this especific serie in this exercise.
                int finalI = i;
                ExerciseRecord previousRecord = lastSessionRecord.stream()
                        .filter(record -> record.getExercise().getId().equals(re.getExercise().getId()
                        )&& record.getSet() == finalI)
                        .findFirst().orElse(null);

                if (previousRecord != null){

                    setDTO.setPreviousWeight(previousRecord.getWeight());
                    setDTO.setPreviousReps(previousRecord.getReps());

                }else {

                    setDTO.setPreviousWeight(re.getInitialWeight());
                    setDTO.setPreviousReps(re.getReps());

                }

                setDTOs.add(setDTO);

            }

            exDTO.setSets(setDTOs);
            exercisesDTO.add(exDTO);

        }

        responseData.setExercises(exercisesDTO);
        return responseData;


    }

    @Transactional
    public void saveCompletedSession(SaveTrainingSessionRequestDTO requestDTO){

        AppUser user = userService.getAuthenticatedUser();
        Routine routine = routineRepo.findById(requestDTO.getRoutineId()).orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        //Create and save session.
        TrainingSession newSession = new TrainingSession();
        newSession.setUser(user);
        newSession.setRoutine(routine);
        newSession.setDurationMinutes(Math.toIntExact(requestDTO.getDurationSeconds() / 60));

        newSession = trainingSessionRepo.save(newSession);

        //Save all the Sets.
        for (SaveExercisesRecordRequestDTO exDTO : requestDTO.getExercises()){

            Exercise exercise = exerciseRepo.findById(exDTO.getExerciseId()).orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
            for (SaveSetRecordRequestDTO setDTO : exDTO.getSets()){

                ExerciseRecord record = new ExerciseRecord();
                record.setSession(newSession);
                record.setExercise(exercise);
                record.setSet(setDTO.getSetNumber());
                record.setWeight(setDTO.getWeight());
                record.setReps(setDTO.getReps());

                exerciseRecordRepo.save(record);

            }

        }

    }

}
