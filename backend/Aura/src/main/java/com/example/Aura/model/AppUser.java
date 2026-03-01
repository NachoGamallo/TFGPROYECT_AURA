package com.example.Aura.model;

import com.example.Aura.model.Enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table ( name = "app_user" )
@Data

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column (nullable = false , name = "name_user")
    private String name;

    @Column (unique = true, nullable = false)
    private String email;

    @Column( name = "password_hash",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)//Para guardarlo como String
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "profile_image_url")
    private String image;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}