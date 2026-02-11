package com.saihoz.task_app.dto.ProjectDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectSimpleResponse(
        UUID id,
        String name,
        LocalDateTime updatedAt
) {
}
