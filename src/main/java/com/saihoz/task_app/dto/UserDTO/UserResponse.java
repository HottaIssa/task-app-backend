package com.saihoz.task_app.dto.UserDTO;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String role,
        String avatar_url
) {
}
