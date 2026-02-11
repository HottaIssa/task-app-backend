package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.CommentDTO.CommentRequest;
import com.saihoz.task_app.dto.CommentDTO.CommentResponse;
import com.saihoz.task_app.dto.KanbanDTO.TaskCardResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskSimpleResponse;
import com.saihoz.task_app.dto.TaskDTO.*;
import com.saihoz.task_app.mapper.CommentMapper;
import com.saihoz.task_app.mapper.TaskMapper;
import com.saihoz.task_app.model.*;
import com.saihoz.task_app.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
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

    private final CommentMapper commentMapper;

    private final ProjectRepo projectRepo;

    public TaskService(TaskRepo taskRepo, UserRepo userRepo, CommentRepo commentRepo, TaskStatusRepo taskStatusRepo, TaskMapper taskMapper, ProjectRepo projectRepo, MemberRepo memberRepo, CommentMapper commentMapper) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.taskStatusRepo = taskStatusRepo;
        this.taskMapper = taskMapper;
        this.projectRepo = projectRepo;
        this.memberRepo = memberRepo;
        this.commentMapper = commentMapper;
    }

    public PagedModel<TaskSimpleResponse> findTasksWithFilter(Long projectId, String status, String priority, String search,
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
        Page<TaskSimpleResponse> responsePage = tasks.map(taskMapper::toTaskSimpleResponse);
        return new PagedModel<>(responsePage);
    }

    public TaskResponse getTask(UUID taskId) {

        Task task = taskRepo.findById(taskId).orElse(null);

        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskCardResponse addTask(TaskRequest request, String username) {
        User currentUser = userRepo.findByUsername(username);

        Project project = projectRepo.findById(request.projectId()).orElse(null);

        TaskStatus status = taskStatusRepo.findByName(request.status());

        Task task = taskMapper.toEntity(request, currentUser, request.priority() != null ? request.priority() : PriorityStatus.MEDIUM, project, status);

        Task savedTask = taskRepo.save(task);
        return taskMapper.toTaskCardResponse(savedTask);
    }

    public TaskCardResponse updateTask(UUID taskId, TaskUpdateRequest request, String username) {
        TaskStatus newStatus = taskStatusRepo.findByName(request.status());

        Task task = taskRepo.findById(taskId).orElse(null);

        ProjectMember newAssignedMember = request.memberId() != null ? memberRepo.findById(request.memberId()).orElse(null) : null;

        task.setTitle(request.title());
        task.setDescription(request.description());
        if (newAssignedMember != null) {
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

        return taskMapper.toTaskCardResponse(updatedTask);
    }

    private Task getTaskIfAdmin(UUID id, String username) {
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        ProjectMember member = memberRepo.findByUserAndProject(user, task.getProject());

        if (!member.hasAdminPermissions()) {
            throw new RuntimeException("No puedes editar, no eres el creador");
        }
        return task;
    }

    public TaskCardResponse patchTaskStatus(UUID id, String username) {
        User user = userRepo.findByUsername(username);
        Task taskToUpdate = taskRepo.findById(id).orElse(null);
        ProjectMember member = memberRepo.findByUserAndProject(user, taskToUpdate.getProject());
        if (!taskToUpdate.canUpdateStatus(member)) throw new RuntimeException("No puedes editar");
        if (taskToUpdate.isCompleted()) throw new RuntimeException("No puedes editar");
        taskToUpdate.setStatus(taskStatusRepo.findById(taskToUpdate.getStatus().getId() + 1).orElse(null));
        taskRepo.save(taskToUpdate);
        return taskMapper.toTaskCardResponse(taskToUpdate);
    }

    public TaskCardResponse patchTaskMember(UUID id, patchTaskMemberRequest request, String username) {
        Task taskToUpdate = getTaskIfAdmin(id, username);
        ProjectMember assignedTo = memberRepo.findById(request.memberId()).orElse(null);
        if (!(taskToUpdate.getProject().isMember(assignedTo.getUser()))) throw new RuntimeException("No puedes editar");
        taskToUpdate.setAssignedTo(assignedTo);
        taskRepo.save(taskToUpdate);
        return taskMapper.toTaskCardResponse(taskToUpdate);
    }

    public TaskCardResponse patchTask(UUID id, PatchTaskRequest request, String username){
        Task taskToUpdate = getTaskIfAdmin(id, username);
        if (request.title() != null && !request.title().isBlank()) {
            taskToUpdate.setTitle(request.title());
        }

        if (request.description() != null) {
            taskToUpdate.setDescription(request.description());
        }

        if (request.priority() != null) {
            taskToUpdate.setPriority(request.priority());
        }

        if (request.dueDate() != null) {
            taskToUpdate.setDueDate(request.dueDate());
        }

        taskRepo.save(taskToUpdate);
        return taskMapper.toTaskCardResponse(taskToUpdate);
    }

    public void deleteTask(UUID id, String username) {
        Task taskToDelete = getTaskIfAdmin(id, username);
        taskRepo.delete(taskToDelete);
    }

    public List<CommentResponse> getCommentsOnTask(UUID id) {
        List<Comment> comments = commentRepo.findByTaskId(id);
        return commentMapper.toListResponse(comments);
    }

    public CommentResponse addCommentOnTask(UUID id, CommentRequest comment, String username) {
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findById(id).orElse(null);
        ProjectMember member = memberRepo.findByUserAndProject(user, task.getProject());
        return commentMapper.toResponse(commentRepo.save(commentMapper.toEntity(comment, member, task)));
    }

    public CommentResponse updateComment(UUID taskId, UUID commentId, CommentRequest request, String username) {
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findById(taskId).orElse(null);
        ProjectMember member = memberRepo.findByUserAndProject(user, task.getProject());
        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (!(member == comment.getMember())) throw new RuntimeException("No tienes los permisos necesarios");
        comment.setContent(request.content());
        return commentMapper.toResponse(commentRepo.save(comment));
    }

    public void deleteComment(UUID taskId, UUID commentId, String username) {
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findById(taskId).orElse(null);
        ProjectMember member = memberRepo.findByUserAndProject(user, task.getProject());
        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (!member.hasAdminPermissions() || !(member == comment.getMember()))
            throw new RuntimeException("No tienes los permisos necesarios");
        commentRepo.delete(comment);
    }

}
