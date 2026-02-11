package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        UUID projectId,
        String projectName,
        UserSimpleResponse assignedTo,
        TaskStatusResponse status,
        String priority,
        LocalDateTime dueDate,
        Boolean isOverdue,
        Double actualHours,
        UserSimpleResponse createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
