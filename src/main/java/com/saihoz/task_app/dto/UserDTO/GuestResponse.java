package com.saihoz.task_app.dto.UserDTO;

import com.saihoz.task_app.dto.ProjectDTO.ProjectSimpleResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record GuestResponse(
        UUID id,
        UserSimpleResponse user,
        ProjectSimpleResponse project,
        LocalDateTime joinedAt
) {
}
