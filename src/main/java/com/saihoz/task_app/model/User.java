package com.saihoz.task_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "The username cannot be empty")
    @Size(max = 20, message = "The username cannot exceed 20 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "The password cannot be empty")
    @Size(min=8, max = 100, message = "The password cannot exceed 20 characters")
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

    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Project> projectsCreated;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @PrePersist
    public void prePersist() {
        this.isActive = true;
    }



}
