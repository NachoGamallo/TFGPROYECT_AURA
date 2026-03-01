package com.example.Aura.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "nutrition_plan")
public class NutritionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private AppUser appUser;

    @Column(nullable = false, name = "target_calories")
    private int calories;

    @Column(nullable = false, name = "target_protein")
    private Double protein;

    @Column(nullable = false, name = "target_carbs")
    private Double carbs;

    @Column(nullable = false, name = "target_fats")
    private Double fats;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "end_date")
    private LocalDateTime endDate;

}
