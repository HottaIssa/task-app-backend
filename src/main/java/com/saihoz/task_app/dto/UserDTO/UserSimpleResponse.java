package com.saihoz.task_app.dto.UserDTO;

import java.util.UUID;

public record UserSimpleResponse(
        UUID id,
        String username,
        String email,
        String fullName,
        String avatar_url
) {
}
