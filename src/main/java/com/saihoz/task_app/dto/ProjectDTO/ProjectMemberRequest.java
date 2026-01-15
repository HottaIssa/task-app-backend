package com.saihoz.task_app.dto.ProjectDTO;

public record ProjectMemberRequest(
        Long userId,
        String role
) {
}
