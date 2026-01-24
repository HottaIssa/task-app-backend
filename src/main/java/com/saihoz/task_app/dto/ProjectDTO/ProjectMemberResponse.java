package com.saihoz.task_app.dto.ProjectDTO;

import com.saihoz.task_app.dto.UserDTO.UserResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectMemberResponse(
        UUID id,
        UserResponse user,
        String roleMember,
        LocalDateTime joinedAt
) {
}
