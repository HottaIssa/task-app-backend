package com.saihoz.task_app.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "The title cannot be empty")
    @Size(max = 200, message = "The title cannot exceed 200 characters")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PriorityStatus priority = PriorityStatus.MEDIUM;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Min(value = 0, message = "The estimated hours cannot be negatives")
    @Column(name = "estimated_hours")
    private Double estimatedHours;

    @Min(value = 0, message = "The actual hours cannot be negatives")
    @Column(name = "actual_hours")
    @Builder.Default
    private Double actualHours = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.priority == null) {
            this.priority = PriorityStatus.MEDIUM;
        }
        if (this.actualHours == null) {
            this.actualHours = 0.0;
        }
    }

    public boolean isOverdue() {
        if (dueDate == null) return false;
        return LocalDateTime.now().isAfter(dueDate) && !isCompleted();
    }

    public boolean isCompleted() {
        return status != null && "DONE".equalsIgnoreCase(status.getName());
    }
}