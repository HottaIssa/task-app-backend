package com.saihoz.task_app.dto.AuthDTO;

public record PatchPasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmationPassword
) {
}
