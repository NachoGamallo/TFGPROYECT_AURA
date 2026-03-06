package com.example.Aura.model.ComposesID;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineExerciceID implements Serializable {

    private UUID routineId;
    private UUID exerciseId;

}
