package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.model.PriorityStatus;

import java.time.LocalDateTime;

public record PatchTaskRequest(
        String title,
        String description,
        PriorityStatus priority,
        LocalDateTime dueDate
) {
}
