package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.model.PriorityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskRequest(
        String title,
        String description,
        UUID projectId,
        String status,
        PriorityStatus priority
) {
}
