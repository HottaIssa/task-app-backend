package com.saihoz.task_app.dto.ProjectDTO;

import com.saihoz.task_app.model.ProjectStatus;

import java.time.LocalDate;

public record ProjectRequest (
    String name,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    ProjectStatus status
){}
