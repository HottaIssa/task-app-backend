package com.saihoz.task_app.dto.TaskDTO;

import java.time.LocalDateTime;

public record patchTaskDueDateRequest(
        LocalDateTime dueDate
) {
}
