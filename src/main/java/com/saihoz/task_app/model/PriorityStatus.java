package com.saihoz.task_app.model;

import lombok.Getter;

@Getter
public enum PriorityStatus {
    URGENT("Urgent"), HIGH("High"), MEDIUM("Medium"), LOW("low");

    private final String displayName;

    PriorityStatus(String displayName) {
        this.displayName = displayName;
    }
}
