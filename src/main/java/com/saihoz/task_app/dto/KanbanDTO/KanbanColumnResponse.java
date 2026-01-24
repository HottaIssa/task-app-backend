package com.saihoz.task_app.dto.KanbanDTO;

import java.util.List;

public record KanbanColumnResponse(
        Long statusId,
        String statusName,
        String statusColor,
        Integer orderIndex,
        Integer taskCount,
        List<TaskCardResponse> tasks
) {
}
