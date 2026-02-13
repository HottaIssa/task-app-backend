package com.saihoz.task_app.dto.NotificationDTO;

import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequest(
        @NotBlank(message = "El mensaje no puede estar vacío")
        String message,

        String type
) {}
