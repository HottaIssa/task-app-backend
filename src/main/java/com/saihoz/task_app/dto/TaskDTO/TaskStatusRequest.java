package com.saihoz.task_app.dto.TaskDTO;

import com.saihoz.task_app.model.TaskStatusCode;

public record TaskStatusRequest(
        TaskStatusCode name,
        String color
) {
}
