package com.saihoz.task_app.dto.ProjectMemberDTO;

import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectMemberResponse(
        UUID id,
        UserSimpleResponse user,
        String roleMember,
        LocalDateTime joinedAt,
        Boolean isActive
) {
}
