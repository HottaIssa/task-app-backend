package com.saihoz.task_app.dto.UserDTO;

public record UserRequest(
        String username,
        String email,
        String password,
        String avatar_url
) {
}
