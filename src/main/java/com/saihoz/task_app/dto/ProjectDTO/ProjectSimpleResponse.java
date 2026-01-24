package com.saihoz.task_app.dto.ProjectDTO;

import java.util.UUID;

public record ProjectSimpleResponse(
        UUID id,
        String name
) {
}
