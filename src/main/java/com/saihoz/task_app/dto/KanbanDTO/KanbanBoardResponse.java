package com.saihoz.task_app.dto.KanbanDTO;

import com.saihoz.task_app.dto.ProjectDTO.ProjectSummaryResponse;

import java.util.List;

public record KanbanBoardResponse(
        List<KanbanColumnResponse> columns,
        ProjectSummaryResponse project
) {
}
