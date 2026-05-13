package com.example.Aura.controller.auth;

import com.example.Aura.dto.request.CreateRoutineRequestDTO;
import com.example.Aura.services.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routines")
@CrossOrigin("*")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @PostMapping("/create")
    public ResponseEntity<String> createRutine(@RequestBody CreateRoutineRequestDTO requestDTO){

        try {

            routineService.createRoutine(requestDTO);
            return ResponseEntity.ok("Rutina creada correctamente");

        }catch (Exception e){

            return ResponseEntity.badRequest().body("Error al crear la rutina");

        }
    }
}
