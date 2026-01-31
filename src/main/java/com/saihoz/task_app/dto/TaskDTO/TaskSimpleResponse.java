package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskSimpleResponse(
        UUID id,
        String title,
        String description,
        String priority,
        TaskStatusResponse status,
        LocalDateTime dueDate,
        Boolean isOverdue,
        UserSimpleResponse assignedTo
) {
}
