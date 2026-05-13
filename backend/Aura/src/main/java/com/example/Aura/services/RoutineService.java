package com.example.Aura.services;

import com.example.Aura.dto.request.CreateRoutineRequestDTO;
import com.example.Aura.dto.request.RoutineExerciseRequestDTO;
import com.example.Aura.model.*;
import com.example.Aura.model.ComposesID.RoutineExerciceID;
import com.example.Aura.repository.ExerciseRepository;
import com.example.Aura.repository.PhysicalProfileRepository;
import com.example.Aura.repository.RoutineExerciseRepository;
import com.example.Aura.repository.RoutineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutineService {

    @Autowired
    UserService service;

    @Autowired
    PhysicalProfileRepository physicalProfileRepository;

    @Autowired
    RoutineRepository routineRepo;

    @Autowired
    ExerciseRepository exerciseRepo;

    @Autowired
    RoutineExerciseRepository routineExerciseRepo;

    @Transactional
    public void createRoutine(CreateRoutineRequestDTO requestDTO){

        AppUser creator = service.getAuthenticatedUser();
        PhysicalProfile profile = physicalProfileRepository.findByAppUser(creator).orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        Routine routine = new Routine();
        routine.setCreator(creator);
        routine.setName(requestDTO.getName());
        routine.setDescription(requestDTO.getDesc());

        //Herencia automatica del user.
        routine.setLevel(profile.getLevel());
        routine.setGoal(profile.getGoal());

        routine = routineRepo.save(routine);

        for (RoutineExerciseRequestDTO exDto : requestDTO.getExercises()){

            Exercise exercise = exerciseRepo.findById(exDto.getExerciseId()).orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
            RoutineExercice re = new RoutineExercice();

            re.setId(new RoutineExerciceID(routine.getId(), exercise.getId()));
            re.setRoutine(routine);
            re.setExercise(exercise);
            re.setPosition(exDto.getPosition());
            re.setSets(exDto.getSets());
            re.setReps(exDto.getReps());
            re.setRestSeconds(exDto.getRestSeconds());
            re.setInitialWeight(exDto.getInitialWeight());

            routineExerciseRepo.save(re);

        }

    }

}
