package com.saihoz.task_app.dto.ProjectDTO;

import java.util.List;

public record ProjectHistoryResponse(
        List<ProjectSimpleResponse> completedProjects,
        List<ProjectSimpleResponse> cancelledProjects
) {
}
