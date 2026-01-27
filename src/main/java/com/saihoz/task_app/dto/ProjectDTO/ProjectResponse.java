package com.saihoz.task_app.dto.ProjectDTO;

import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.model.ProjectStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        UserResponse createdBy
) {
}
