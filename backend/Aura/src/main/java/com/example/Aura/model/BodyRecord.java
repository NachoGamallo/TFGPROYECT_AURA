package com.example.Aura.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table( name = "body_record" )
@Data

public class BodyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private AppUser user;

    @Column(name = "weight_kg", nullable = false)
    private double weight;

    @CreationTimestamp
    @Column(name = "record_date", updatable = false)
    private LocalDateTime createdAt;

}
