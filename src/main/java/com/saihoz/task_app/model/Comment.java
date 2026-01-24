package com.saihoz.task_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 200, message = "The content cannot exceed 200 characters")
    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_edited", nullable = false)
    @Builder.Default
    private Boolean isEdited = false;

    public boolean belongsToUser(User user) {
        if (user == null || this.user == null) {
            return false;
        }
        return this.user.equals(user);
    }

    public boolean wasEdited() {
        return isEdited != null && isEdited;
    }

    public void markAsEdited() {
        this.isEdited = true;
    }

    public String getPreview() {
        if (content == null) return "";
        return content.length() > 100 ? content.substring(0, 100) + "..." : content;
    }

    public boolean canBeEditedBy(User user) {
        if (user == null) return false;

        // Solo el autor puede editar su comentario
        return belongsToUser(user);
    }

    public boolean canBeDeletedBy(User user) {
        if (user == null) return false;

        // El autor puede eliminar
        if (belongsToUser(user)) {
            return true;
        }

        // El admin del proyecto puede eliminar
        return task != null && task.getProject().isAdmin(user);
    }

    public String getTimeAgo() {
        if (createdAt == null) return "Unknown";

        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(createdAt, now).toMinutes();

        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minutes ago";

        long hours = minutes / 60;
        if (hours < 24) return hours + " hours ago";

        long days = hours / 24;
        if (days < 30) return days + " days ago";

        long months = days / 30;
        return months + " months ago";
    }

}
