package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.KanbanDTO.TaskCardResponse;
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
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
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

    private final MemberRepo memberRepo;

    private final TaskStatusRepo taskStatusRepo;

    private final TaskMapper taskMapper;

    private final ProjectRepo projectRepo;

    public TaskService(TaskRepo taskRepo, UserRepo userRepo, CommentRepo commentRepo, TaskStatusRepo taskStatusRepo, TaskMapper taskMapper, ProjectRepo projectRepo, MemberRepo memberRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.taskStatusRepo = taskStatusRepo;
        this.taskMapper = taskMapper;
        this.projectRepo = projectRepo;
        this.memberRepo = memberRepo;
    }

    public PagedModel<TaskCardResponse> findTasksWithFilter(Long projectId, String status, String priority, String search,
                                                            LocalDateTime dueFrom, LocalDateTime dueTo, Boolean isOverdue, Boolean isAssignedToMe, Boolean isCreatedByMe, String username, Pageable pageable) {
        User user = userRepo.findByUsername(username);

        Specification<Task> spec = Specification
                .where(TaskSpecification.isOwnerOrAssignee(user))
                .and(TaskSpecification.projectId(projectId))
                .and(TaskSpecification.status(taskStatusRepo.findByName(status)))
                .and(TaskSpecification.priority(priority))
                .and(TaskSpecification.search(search))
                .and(TaskSpecification.dueBetween(dueFrom, dueTo))
                .and(TaskSpecification.isOverdue(isOverdue))
                .and(TaskSpecification.isAssignedToMe(isAssignedToMe, user))
                .and(TaskSpecification.isCreatedByMe(isCreatedByMe, user));

        Page<Task> tasks = taskRepo.findAll(spec, pageable);
        Page<TaskCardResponse> responsePage = tasks.map(taskMapper::toTaskCardResponse);
        return new PagedModel<>(responsePage);
    }

    public TaskResponse getTask(UUID taskId) {

        Task task = taskRepo.findById(taskId).orElse(null);

        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse addTask(TaskRequest request, String username) {
        User currentUser = userRepo.findByUsername(username);

        Project project = projectRepo.findById(request.projectId()).orElse(null);

        TaskStatus status = taskStatusRepo.findByName(request.status());

        Task task = taskMapper.toEntity(request, currentUser, request.priority() != null ? request.priority() : PriorityStatus.MEDIUM, project, status);

        Task savedTask = taskRepo.save(task);
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse updateTask(UUID taskId, TaskUpdateRequest request, String username) {
        User user = userRepo.findByUsername(username);

        TaskStatus newStatus = taskStatusRepo.findByName(request.status());

        Task task = taskRepo.findById(taskId).orElse(null);

        ProjectMember newAssignedMember = request.memberId() != null ? memberRepo.findById(request.memberId()).orElse(null) : null;

        task.setTitle(request.title());
        task.setDescription(request.description());
        if(newAssignedMember != null){
            task.setAssignedTo(task.getProject().isMember(newAssignedMember.getUser()) ? newAssignedMember : null);
        } else {
            task.setAssignedTo(null);
        }
        task.setStatus(newStatus);
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());

        Task updatedTask = taskRepo.save(task);

        // TODO: Crear notificaciones si cambió el asignado o estado
        // TODO: Enviar WebSocket broadcast
        // TODO: Registrar cambios en activity log

        return taskMapper.toResponse(updatedTask);
    }

    public TaskResponse patchTaskStatus(UUID id, String username) {
        User user = userRepo.findByUsername(username);
        Task taskToUpdate = taskRepo.findById(id).orElse(null);
        ProjectMember member = memberRepo.findByUserAndProject(user, taskToUpdate.getProject());
        if(!taskToUpdate.canUpdateStatus(member) || !member.hasAdminPermissions()) throw new RuntimeException("No puedes editar");
        if(taskToUpdate.isCompleted()) throw new RuntimeException("No puedes editar");
        taskToUpdate.setStatus(taskStatusRepo.findById(taskToUpdate.getStatus().getId() + 1).orElse(null));
        taskRepo.save(taskToUpdate);
        return taskMapper.toResponse(taskToUpdate);
    }

    public void deleteTask(UUID id, String username) {
        Task taskToDelete = taskRepo.findById(id).orElse(null);
        if(!taskToDelete.getCreatedBy().getUsername().equals(username)) throw new RuntimeException("No puedes eliminar");
        taskRepo.delete(taskToDelete);
    }

    public List<Comment> getCommentsOnTask(UUID id) {
        Task task = taskRepo.findById(id).orElse(null);
        return task.getComments();
    }

    public Comment addCommentOnTask(UUID id, Comment comment, String username) {
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findById(id).orElse(null);
        comment.setTask(task);
        comment.setUser(user);
        return commentRepo.save(comment);
    }

}
