package com.saihoz.task_app.dto.NotificationDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String message,
        String type,
        UUID taskId,
        String taskTitle,
        UUID projectId,
        boolean isRead,
        LocalDateTime createdAt
) {}
