package com.saihoz.task_app.dto.NotificationDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationMessage(
        UUID id,
        UUID userId,
        UUID taskId,
        String taskTitle,
        String message,
        String type,
        @JsonProperty("isRead")
        boolean isRead,
        LocalDateTime createdAt,
        LocalDateTime timestamp
) {}
