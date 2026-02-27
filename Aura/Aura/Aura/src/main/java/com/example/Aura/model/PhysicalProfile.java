package com.example.Aura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "physical_profile")
public class PhysicalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private AppUser appUser;

    @Column(nullable = false, name = "height_cm")
    private Integer heightCM;

    @Column(nullable = false, name = "initial_weight")
    private Integer initialWeight;

    @Column(nullable = false, name = "user_gender")
    private Gender gender;

    @Column(nullable = false, name = "user_level")
    private Level level = Level.BEGINNER;

    @Column(nullable = false, name = "age")
    private Integer age;

}
