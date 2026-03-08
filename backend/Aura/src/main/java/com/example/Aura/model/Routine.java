package com.example.Aura.model;

import com.example.Aura.model.Enums.Goal;
import com.example.Aura.model.Enums.UserLevel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "routine")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private AppUser creator;

    @Column(nullable = false, name = "name")
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private UserLevel level;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "routine" , cascade = CascadeType.ALL)
    private List<RoutineExercice> exercises;

}
