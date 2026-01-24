package com.saihoz.task_app.dto.ProjectDTO;

import com.saihoz.task_app.model.ProjectStatus;

import java.time.LocalDateTime;

public record ProjectRequest (
    String name,
    String description,
    LocalDateTime startDate,
    LocalDateTime endDate,
    ProjectStatus status
){}
