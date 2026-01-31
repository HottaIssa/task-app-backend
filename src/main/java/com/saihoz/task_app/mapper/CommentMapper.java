package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.CommentDTO.CommentRequest;
import com.saihoz.task_app.dto.CommentDTO.CommentResponse;
import com.saihoz.task_app.model.Comment;
import com.saihoz.task_app.model.ProjectMember;
import com.saihoz.task_app.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final ProjectMemberMapper memberMapper;

    public CommentMapper(ProjectMemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public CommentResponse toResponse(Comment comment){
        return new CommentResponse(
                comment.getId(),
                memberMapper.toResponse(comment.getMember()),
                comment.getPreview(),
                comment.getTimeAgo(),
                comment.getIsEdited()
        );
    }

    public Comment toEntity(CommentRequest comment, ProjectMember member, Task task){
        Comment newComment = new Comment();
        newComment.setMember(member);
        newComment.setTask(task);
        newComment.setContent(comment.content());
        return newComment;
    }

    public List<CommentResponse> toListResponse(List<Comment> comments){
        return comments.stream().map(this::toResponse).collect(Collectors.toList());
    }

}
