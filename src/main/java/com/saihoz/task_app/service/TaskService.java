package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.TaskDTO.PatchTaskStatusRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskUpdateRequest;
import com.saihoz.task_app.mapper.TaskMapper;
import com.saihoz.task_app.model.*;
import com.saihoz.task_app.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TaskService {

    private final TaskRepo taskRepo;

    private final UserRepo userRepo;

    private final CommentRepo commentRepo;

    private final TaskStatusRepo taskStatusRepo;

    private final TaskMapper taskMapper;

    private final ProjectRepo projectRepo;

    public TaskService(TaskRepo taskRepo, UserRepo userRepo, CommentRepo commentRepo, TaskStatusRepo taskStatusRepo, TaskMapper taskMapper, ProjectRepo projectRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.taskStatusRepo = taskStatusRepo;
        this.taskMapper = taskMapper;
        this.projectRepo = projectRepo;
    }

    public Page<TaskResponse> findTasksWithFilter(Long projectId, String status, String priority, String search,
                                          LocalDateTime dueFrom, LocalDateTime dueTo, Boolean isOverdue, String username, Pageable pageable){

        Specification<Task> spec = Specification
                .where(TaskSpecification.projectId(projectId))
                .and(TaskSpecification.status(status))
                .and(TaskSpecification.priority(priority))
                .and(TaskSpecification.search(search))
                .and(TaskSpecification.dueBetween(dueFrom, dueTo))
                .and(TaskSpecification.isOverdue(isOverdue));

        User user = userRepo.findByUsername(username);
        Page<Task> tasks = taskRepo.findByCreatedBy(user, spec, pageable);
        return tasks.map(taskMapper::toResponse);
    }

    public TaskResponse getTask(UUID taskId) {

        Task task = taskRepo.findById(taskId).orElse(null);

        return taskMapper.toResponse(task);
    }

    public TaskResponse addTask(TaskRequest request, String username) {
        User currentUser = userRepo.findByUsername(username);

        Project project = projectRepo.findById(request.projectId()).orElse(null);

        TaskStatus status = taskStatusRepo.findById(request.statusId()).orElse(null);

        User assignedUser = null;
        if (request.memberId() != null) {
            assignedUser = userRepo.findById(request.memberId()).orElse(null);
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .project(project)
                .assignedTo(assignedUser)
                .status(status)
                .priority(request.priority() != null ? request.priority() : PriorityStatus.MEDIUM)
                .dueDate(request.dueDate())
                .estimatedHours(request.estimatedHours())
                .actualHours(0.0)
                .createdBy(currentUser)
                .build();

        Task savedTask = taskRepo.save(task);

        // TODO: Crear notificación si hay usuario asignado
        // TODO: Enviar WebSocket broadcast
        // TODO: Registrar en activity log

        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse updateTask(UUID taskId, TaskUpdateRequest request, String username) {
        // Obtener usuario actual
        User currentUser = userRepo.findByUsername(username);

        // Obtener la tarea
        Task task = taskRepo.findById(taskId).orElse(null);

        // Validar nuevo estado
        TaskStatus newStatus = taskStatusRepo.findById(request.status()).orElse(null);

        // Validar usuario asignado (si cambió)
        User newAssignedUser = null;
        if (request.memberId() != null) {
            newAssignedUser = userRepo.findById(request.memberId()).orElse(null);
        }

        // Actualizar campos
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setAssignedTo(newAssignedUser);
        task.setStatus(newStatus);
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());
        task.setEstimatedHours(request.estimatedHours());
        task.setActualHours(request.actualHours());

        Task updatedTask = taskRepo.save(task);

        // TODO: Crear notificaciones si cambió el asignado o estado
        // TODO: Enviar WebSocket broadcast
        // TODO: Registrar cambios en activity log

        return taskMapper.toResponse(updatedTask);
    }

    public TaskResponse patchTaskStatus(UUID id, PatchTaskStatusRequest request, String username){
        Task taskToUpdate = taskRepo.findByIdAndCreatedBy(id, userRepo.findByUsername(username));
        taskToUpdate.setStatus(taskStatusRepo.findById(request.statusId()).orElse(null));
        taskRepo.save(taskToUpdate);
        return taskMapper.toResponse(taskToUpdate);
    }

    public void deleteTask(UUID id, String username){
        Task taskToDelete = taskRepo.findByIdAndCreatedBy(id, userRepo.findByUsername(username));
        taskRepo.delete(taskToDelete);
    }

    public List<Comment> getCommentsOnTask(UUID id){
        Task task = taskRepo.findById(id).orElse(null);
        return task.getComments();
    }

    public Comment addCommentOnTask(UUID id, Comment comment, String username){
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findById(id).orElse(null);
        comment.setTask(task);
        comment.setUser(user);
        return commentRepo.save(comment);
    }

}
