package com.saihoz.task_app.dto.KanbanDTO;


import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberSimpleResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskCardResponse(
        UUID id,
        String title,
        String priority,
        LocalDateTime dueDate,
        Boolean isOverdue,
        ProjectMemberSimpleResponse assignedTo
) {
}
