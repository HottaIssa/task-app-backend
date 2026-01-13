package com.saihoz.task_app.dto;

public record RegisterResponse(
        Long id,
        String username,
        String email,
        String password
)
{}
