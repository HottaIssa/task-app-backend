package com.saihoz.task_app.dto.ProjectDTO;

import com.saihoz.task_app.model.ProjectStatus;
import com.saihoz.task_app.model.RoleMember;

import java.util.UUID;

public record ProjectSummaryResponse(
        UUID id,
        String name,
        ProjectStatus status,
        Boolean isOwner,
        Integer totalTasks,
        Integer completeTasks,
        RoleMember roleMember
) {
}
