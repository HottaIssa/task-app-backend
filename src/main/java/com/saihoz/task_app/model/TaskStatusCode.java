package com.saihoz.task_app.model;

import lombok.Getter;

@Getter
public enum TaskStatusCode {
    TODO("Todo"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String displayName;

    TaskStatusCode(String displayName) {
        this.displayName = displayName;
    }
}
