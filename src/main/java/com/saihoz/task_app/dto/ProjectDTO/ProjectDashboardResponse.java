package com.saihoz.task_app.dto.ProjectDTO;

import java.util.List;

public record ProjectDashboardResponse(
    List<ProjectResponse> activeProjects,
    List<ProjectResponse> onHoldProjects,
    List<ProjectResponse> recentCompletedProjects
) {
}
