package com.saihoz.task_app.dto.ProjectDTO;

import com.saihoz.task_app.model.ProjectStatus;

public record PatchProjectStatusRequest(
        ProjectStatus status
) {
}
