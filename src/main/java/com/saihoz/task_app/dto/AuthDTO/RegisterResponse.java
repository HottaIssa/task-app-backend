package com.saihoz.task_app.dto.AuthDTO;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        String role,
        String avatar_url
)
{}
