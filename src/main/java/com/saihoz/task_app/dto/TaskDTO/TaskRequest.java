package com.saihoz.task_app.dto.TaskDTO;

import java.time.LocalDateTime;

public record TaskRequest(
        String title,
        String description,
        Long projectId,
        Long memberId,
        Long status,
        String priority,
        LocalDateTime dueDate,
        Double estimatedHours
) {
}
