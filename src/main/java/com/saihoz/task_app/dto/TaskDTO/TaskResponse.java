package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;
import com.saihoz.task_app.model.PriorityStatus;

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
        PriorityStatus priority,
        LocalDateTime dueDate,
        Double estimatedHours,
        Double actualHours,
        UserSimpleResponse createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
