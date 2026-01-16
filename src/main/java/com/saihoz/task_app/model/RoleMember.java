package com.saihoz.task_app.model;

import lombok.Getter;

@Getter
public enum RoleMember {
    ADMIN("Admin"), MEMBER("Member"), VIEWER("Viewer");

    private final String displayName;

    RoleMember(String displayName) {
        this.displayName = displayName;
    }
}
