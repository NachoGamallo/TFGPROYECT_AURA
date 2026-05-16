package com.example.Aura.controller.auth;

import com.example.Aura.dto.request.CreateBodyRecordRequestDTO;
import com.example.Aura.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/record")
@CrossOrigin(origins = "*")
public class BodyRecordController {

    @Autowired
    UserService userService;

    @PostMapping("/body-record")
    public ResponseEntity<Map<String,String>> createBodyRecord(@RequestBody CreateBodyRecordRequestDTO request){

        try {
            userService.createdBodyRecord(request);
            return ResponseEntity.ok(Map.of("message", "Peso registrado correctamente."));

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));

        }

    }

}
