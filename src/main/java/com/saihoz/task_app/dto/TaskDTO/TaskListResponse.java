package com.saihoz.task_app.dto.TaskDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskListResponse(
        UUID id,
        String title,
        String description,
        String status,
        String priority,
        LocalDateTime dueDate
) {
}
