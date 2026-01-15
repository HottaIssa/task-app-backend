package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.dto.UserDTO.UserResponse;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Long projectId,
        String projectName,
        UserResponse member,
        TaskStatusResponse status,
        String priority,
        LocalDateTime dueDate,
        Double estimatedHours,
        Double actualHours
) {
}
