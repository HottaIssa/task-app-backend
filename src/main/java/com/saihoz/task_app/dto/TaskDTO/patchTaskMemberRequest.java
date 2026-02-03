package com.saihoz.task_app.dto.TaskDTO;

import java.util.UUID;

public record patchTaskMemberRequest(
        UUID memberId
) {
}
