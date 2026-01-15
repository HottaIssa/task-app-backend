package com.saihoz.task_app.dto.ProjectDTO;

import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.model.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        UserResponse createdBy
) {
}
