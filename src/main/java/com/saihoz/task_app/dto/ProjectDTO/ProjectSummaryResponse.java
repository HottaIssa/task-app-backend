package com.saihoz.task_app.dto.ProjectDTO;

import java.util.UUID;

public record ProjectSummaryResponse(
        UUID id,
        String name,
        Integer totalTasks,
        Integer completeTasks
) {
}
