package com.saihoz.task_app.dto.TaskDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskRequest(
        String title,
        String description,
        UUID projectId,
        UUID memberId,
        Long status,
        String priority,
        LocalDateTime dueDate,
        Double estimatedHours
) {
}
