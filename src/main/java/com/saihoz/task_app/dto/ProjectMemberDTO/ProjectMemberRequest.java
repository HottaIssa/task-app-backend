package com.saihoz.task_app.dto.ProjectMemberDTO;

import com.saihoz.task_app.model.RoleMember;

import java.util.UUID;

public record ProjectMemberRequest(
        UUID userId,
        RoleMember role
) {
}
