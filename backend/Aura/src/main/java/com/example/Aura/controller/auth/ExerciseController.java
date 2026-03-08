package com.example.Aura.controller.auth;

import com.example.Aura.dto.response.ExerciseDetailsDTO;
import com.example.Aura.dto.response.ExerciseSummaryDTO;
import com.example.Aura.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin("*")
public class ExerciseController {

    @Autowired
    ExerciseService exerciseService;

    @GetMapping
    public List<ExerciseSummaryDTO> getAll(){

        return exerciseService.getAllExercises();

    }

    @GetMapping("{id}")
    public ExerciseDetailsDTO getDetails(@PathVariable UUID id){

        return exerciseService.getExerciseDetailsById(id);

    }

}
