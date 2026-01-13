package com.saihoz.task_app.model;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    ACTIVE("Active"), ARCHIVED("Archived"), DELETED("Deleted");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }
}
