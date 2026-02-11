package com.saihoz.task_app.dto.ProjectDTO;

import java.util.List;

public record ProjectHistoryResponse(
        List<ProjectResponse> completedProjects,
        List<ProjectResponse> cancelledProjects
) {
}
