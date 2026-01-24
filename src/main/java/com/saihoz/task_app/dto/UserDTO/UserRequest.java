package com.saihoz.task_app.dto.UserDTO;

public record UserRequest(
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        String avatar_url
) {
}
