package com.example.Aura.services;

import com.example.Aura.dto.response.ExerciseDetailsDTO;
import com.example.Aura.dto.response.ExerciseSummaryDTO;
import com.example.Aura.model.Enums.ExerciseType;
import com.example.Aura.model.Exercise;
import com.example.Aura.model.ExerciseMuscle;
import com.example.Aura.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepo;

    //Para la lista general de ejercicios.
    public List<ExerciseSummaryDTO> getAllExercises() {
        return exerciseRepo.findAll().stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    //Para el detalle del ejercicio.
    public ExerciseDetailsDTO getExerciseDetailsById(UUID id){
        Exercise exercise = exerciseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
        return convertToDetailDTO(exercise);
    }

    private ExerciseSummaryDTO convertToSummaryDTO(Exercise exercise) {

        ExerciseSummaryDTO dto = new ExerciseSummaryDTO();

        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setImageURL(exercise.getImageURL());

        //Nombre del musculo primario.

        exercise.getExerciseMuscles().stream()
                .filter(ExerciseMuscle::getIsPrimary)
                .findFirst()
                .ifPresent(em -> dto.setPrimaryMuscle(em.getMuscleGroup().getName()));

        return dto;

    }

    private ExerciseDetailsDTO convertToDetailDTO(Exercise exercise) {
        ExerciseDetailsDTO dto = new ExerciseDetailsDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setDesc(exercise.getDescription());
        dto.setExerciseType(ExerciseType.valueOf(exercise.getExerciseType().toString()));
        dto.setEquipment(exercise.getEquipment());
        dto.setImgURL(exercise.getImageURL());

        // Mapeamos los músculos filtrando por primarios/secundarios
        dto.setPrimaryMuscles(exercise.getExerciseMuscles().stream()
                .filter(ExerciseMuscle::getIsPrimary)
                .map(em -> em.getMuscleGroup().getName())
                .collect(Collectors.toList()));

        dto.setSecondaryMuscles(exercise.getExerciseMuscles().stream()
                .filter(em -> !em.getIsPrimary())
                .map(em -> em.getMuscleGroup().getName())
                .collect(Collectors.toList()));

        // Mapeamos las instrucciones (ya vienen ordenadas si pusiste el @OrderBy)
        dto.setInstructions(exercise.getInstructions().stream()
                .map(i -> i.getText())
                .collect(Collectors.toList()));

        return dto;
    }

}
