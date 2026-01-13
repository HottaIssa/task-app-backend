package com.saihoz.task_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The username cannot be empty")
    @Size(max = 50, message = "The username cannot exceed 50 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "The password cannot be empty")
    @Size(max = 50, message = "The password cannot exceed 50 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "The email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "The first name cannot be empty")
    @Size(max = 50, message = "The first name cannot exceed 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "The last name cannot be empty")
    @Size(max = 50, message = "The last name cannot exceed 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = true)
    private String avatar_url;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Project> projectCreated;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ProjectMember> projectMember;

}
