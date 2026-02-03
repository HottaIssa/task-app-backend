package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.KanbanDTO.TaskCardResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskSimpleResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskStatusResponse;
import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;
import com.saihoz.task_app.model.*;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final ProjectMemberMapper projectMemberMapper;

    private final UserMapper userMapper;

    public TaskMapper(ProjectMemberMapper projectMemberMapper, UserMapper userMapper) {
        this.projectMemberMapper = projectMemberMapper;
        this.userMapper = userMapper;
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getAssignedTo() != null ? projectMemberMapper.toSimpleResponse(task.getAssignedTo()) : null,
                new TaskStatusResponse(
                        task.getStatus().getId(),
                        task.getStatus().getName(),
                        task.getStatus().getColor(),
                        task.getStatus().getOrderIndex()),
                task.getPriority().getDisplayName(),
                task.getDueDate(),
                task.isOverdue(),
                task.getActualHours(),
                userMapper.toSimpleResponse(task.getCreatedBy()),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public Task toEntity(TaskRequest request, User user, PriorityStatus priorityStatus, Project project, TaskStatus taskStatus) {
        Task taskToSave = new Task();
        taskToSave.setTitle(request.title());
        taskToSave.setDescription(request.description());
        taskToSave.setProject(project);
        taskToSave.setStatus(taskStatus);
        taskToSave.setAssignedTo(null);
        taskToSave.setPriority(priorityStatus);
        taskToSave.setDueDate(null);
        taskToSave.setCreatedBy(user);
        taskToSave.setActualHours(0.0);
        return taskToSave;
    }

    public TaskCardResponse toTaskCardResponse(Task task) {
        return new TaskCardResponse(
                task.getId(),
                task.getTitle(),
                task.getPriority().getDisplayName(),
                task.getDueDate(),
                task.isOverdue(),
                task.getAssignedTo() != null ?
                        projectMemberMapper.toSimpleResponse(task.getAssignedTo()) : null
        );
    }

    public TaskSimpleResponse toTaskSimpleResponse(Task task) {
        return new TaskSimpleResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority().getDisplayName(),
                new TaskStatusResponse(
                        task.getStatus().getId(),
                        task.getStatus().getName(),
                        task.getStatus().getColor(),
                        task.getStatus().getOrderIndex()),
                task.getDueDate(),
                task.isOverdue(),
                task.getAssignedTo() != null ?
                        userMapper.toSimpleResponse(task.getAssignedTo().getUser())
                        :
                        null
        );
    }
}
