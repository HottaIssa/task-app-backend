package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.dto.UserDTO.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        UUID projectId,
        String projectName,
        UserResponse member,
        TaskStatusResponse status,
        String priority,
        LocalDateTime dueDate,
        Double estimatedHours,
        Double actualHours
) {
}
