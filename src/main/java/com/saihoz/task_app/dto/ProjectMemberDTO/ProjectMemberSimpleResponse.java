package com.saihoz.task_app.dto.ProjectMemberDTO;

import java.util.UUID;

public record ProjectMemberSimpleResponse(
        UUID id,
        String username,
        String fullName,
        String email,
        String avatar_url
) {
}
