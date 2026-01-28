package com.leonardo.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "full_name", nullable = false)
    @jakarta.validation.constraints.NotBlank
    private String fullName;

    @Column(nullable = false, unique = true)
    @jakarta.validation.constraints.Email
    private String email;

    @Column(nullable = false)
    @jakarta.validation.constraints.Size(min = 8)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}