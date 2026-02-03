package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.model.PriorityStatus;

public record patchTaskPriorityRequest(
        PriorityStatus priority
) {
}
