package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.KanbanDTO.TaskCardResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskListResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskStatusResponse;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;
import com.saihoz.task_app.model.*;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskListResponse toListResponse(Task task){
        return new TaskListResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().getName(),
                task.getPriority().getDisplayName(),
                task.getDueDate()
        );
    }

    public TaskResponse toResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getProject().getId(),
                task.getProject().getName(),
                new UserResponse(
                        task.getAssignedTo().getId(),
                        task.getAssignedTo().getUsername(),
                        task.getAssignedTo().getEmail(),
                        task.getAssignedTo().getFirstName(),
                        task.getAssignedTo().getLastName(),
                        task.getAssignedTo().getRole().getDisplayName(),
                        task.getAssignedTo().getAvatar_url()),
                new TaskStatusResponse(
                        task.getStatus().getId(),
                        task.getStatus().getName(),
                        task.getStatus().getColor(),
                        task.getStatus().getOrderIndex()),
                task.getPriority(),
                task.getDueDate(),
                task.getEstimatedHours(),
                task.getActualHours(),
                new UserSimpleResponse(
                        task.getAssignedTo().getId(),
                        task.getAssignedTo().getUsername(),
                        task.getAssignedTo().getEmail()
                ),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public Task toEntity(TaskRequest request, User user, PriorityStatus priorityStatus, Project project, TaskStatus taskStatus, User member){
        Task taskToSave = new Task();
        taskToSave.setTitle(request.title());
        taskToSave.setDescription(request.description());
        taskToSave.setProject(project);
        taskToSave.setStatus(taskStatus);
        taskToSave.setAssignedTo(member);
        taskToSave.setPriority(priorityStatus);
        taskToSave.setDueDate(request.dueDate());
        taskToSave.setEstimatedHours(request.estimatedHours());
        taskToSave.setCreatedBy(user);
        return taskToSave;
    }

    public TaskCardResponse toTaskCardResponse(Task task){
        return new TaskCardResponse(
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
                new UserSimpleResponse(
                        task.getAssignedTo().getId(),
                        task.getAssignedTo().getUsername(),
                        task.getAssignedTo().getEmail()
                )
        );
    }
}
