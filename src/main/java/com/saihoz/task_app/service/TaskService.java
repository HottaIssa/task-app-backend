package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.TaskDTO.PatchTaskStatusRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
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

    public TaskResponse getTask(UUID id, String username){
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findByIdAndCreatedBy(id, user);
        return taskMapper.toResponse(task);
    }

    public TaskResponse addTask(TaskRequest task, String username){
        User user = userRepo.findByUsername(username);
        User member = userRepo.findById(task.memberId()).orElse(null);
        PriorityStatus priorityStatus = PriorityStatus.valueOf(task.priority().toUpperCase());
        TaskStatus taskStatus = taskStatusRepo.findById(task.status()).orElse(null);
        Project project = projectRepo.findById(task.projectId()).orElse(null);
        Task taskToSave = taskMapper.toEntity(task, user, priorityStatus, project, taskStatus, member);
        taskRepo.save(taskToSave);
        return taskMapper.toResponse(taskToSave);
    }

    public TaskResponse updateTask(UUID id, Task task, String username){
        Task taskToUpdate = taskRepo.findByIdAndCreatedBy(id, userRepo.findByUsername(username));
        taskRepo.save(task);
        return taskMapper.toResponse(taskToUpdate);
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
        return task.getComment();
    }

    public Comment addCommentOnTask(UUID id, Comment comment, String username){
        User user = userRepo.findByUsername(username);
        Task task = taskRepo.findById(id).orElse(null);
        comment.setTask(task);
        comment.setUser(user);
        return commentRepo.save(comment);
    }

}
