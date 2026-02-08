package com.saihoz.task_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "The name of the project cannot be empty")
    @Size(max = 50, message = "The name of the project cannot exceed 50 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "The description of the project cannot be empty")
    @Size(max = 200, message = "The description of the project cannot exceed 200 characters")
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.ACTIVE; // ✅ Valor por defecto

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }

        if (createdBy.equals(user)) {
            return true;
        }

        return members.stream()
                .anyMatch(member -> member.getUser().equals(user) && member.getIsActive());
    }

    public boolean isAdmin(User user) {
        if (user == null) {
            return false;
        }

        if (createdBy.equals(user)) {
            return true;
        }

        return members.stream()
                .anyMatch(member -> member.getUser().equals(user) &&
                        member.getRoleMember() == RoleMember.ADMIN);
    }

    public boolean isOwner(User user){
        if(user == null){
            return false;
        }

        return createdBy.equals(user);
    }

    public RoleMember getUserRole(User user) {
        if (user == null) {
            return null;
        }

        // El creador es owner
        if (createdBy.equals(user)) {
            return RoleMember.ADMIN;
        }

        // Buscar en la lista de miembros
        return members.stream()
                .filter(member -> member.getUser().equals(user))
                .findFirst()
                .map(ProjectMember::getRoleMember)
                .orElse(null);
    }

    public int getTotalTasks() {
        return tasks != null ? tasks.size() : 0;
    }

    public long getCompletedTasksCount() {
        if (tasks == null) return 0;
        return tasks.stream()
                .filter(Task::isCompleted)
                .count();
    }

    public long getMembersCount() {
        if (members == null) return 1; // Al menos el creador
        return members.stream()
                .count() + 1; // +1 por el creador
    }

    public boolean isActive() {
        return status == ProjectStatus.ACTIVE;
    }

    public boolean isArchived() {
        return status == ProjectStatus.ARCHIVED;
    }
}