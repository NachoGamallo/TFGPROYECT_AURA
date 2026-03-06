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
public class ExerciseMuscleID implements Serializable {

    private UUID exerciseId;
    private UUID muscleGroupId;

}
