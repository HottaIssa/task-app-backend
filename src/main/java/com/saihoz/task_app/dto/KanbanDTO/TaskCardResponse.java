package com.saihoz.task_app.dto.KanbanDTO;


import com.saihoz.task_app.dto.TaskDTO.TaskStatusResponse;
import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;
import com.saihoz.task_app.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCardResponse(
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
