package com.saihoz.task_app.dto.CommentDTO;

import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberResponse;

import java.util.UUID;

public record CommentResponse(
        UUID id,
        ProjectMemberResponse member,
        String content,
        String timeAgo,
        boolean isEdited
) {
}
