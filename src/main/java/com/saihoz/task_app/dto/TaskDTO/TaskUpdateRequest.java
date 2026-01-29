package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.model.PriorityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskUpdateRequest(
        String title,
        String description,
        UUID memberId,
        String status,
        PriorityStatus priority,
        LocalDateTime dueDate,
        Double actualHours
) {
}
