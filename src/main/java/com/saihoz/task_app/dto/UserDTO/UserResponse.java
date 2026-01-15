package com.saihoz.task_app.dto.UserDTO;

public record UserResponse(
        Long id,
        String username,
        String email,
        String avatar_url
) {
}
