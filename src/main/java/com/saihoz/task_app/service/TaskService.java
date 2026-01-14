package com.saihoz.task_app.service;

import com.saihoz.task_app.model.Comment;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.repo.CommentRepo;
import com.saihoz.task_app.repo.TaskRepo;
import com.saihoz.task_app.repo.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    private final UserRepo userRepo;

    private final CommentRepo commentRepo;

    public TaskService(TaskRepo taskRepo, UserRepo userRepo, CommentRepo commentRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
    }

    public Page<Task> findTasksWithFilter(Long projectId, String status, Long assignedTo, String priority, String search,
                                          LocalDateTime dueFrom, LocalDateTime dueTo, Boolean isOverdue, String username, Pageable pageable){

        Specification<Task> spec = Specification
                .where(TaskSpecification.projectId(projectId))
                .and(TaskSpecification.status(status))
                .and(TaskSpecification.assignedTo(assignedTo))
                .and(TaskSpecification.priority(priority))
                .and(TaskSpecification.search(search))
                .and(TaskSpecification.dueBetween(dueFrom, dueTo))
                .and(TaskSpecification.isOverdue(isOverdue));

        User user = userRepo.findByUsername(username);
        return taskRepo.findByCreatedBy(user, spec, pageable);
    }

    public Task getTask(Long id, String username){
        User user = userRepo.findByUsername(username);
        return taskRepo.findByIdAndCreatedBy(id, user);
    }

    public Task addTask(Long id, Task task, String username){
        User user = userRepo.findByUsername(username);
        task.setCreatedBy(user);
        return taskRepo.save(task);
    }

    public Task updateTask(Long id, Task task, String username){
        Task taskToUpdate = taskRepo.findByIdAndCreatedBy(id, userRepo.findByUsername(username));
        return taskRepo.save(task);
    }

    public void deleteTask(Long id, String username){
        Task taskToDelete = taskRepo.findByIdAndCreatedBy(id, userRepo.findByUsername(username));
        taskRepo.delete(taskToDelete);
    }

    public List<Comment> getCommentsOnTask(Long id, String username){
        Task task = taskRepo.findByIdAndCreatedBy(id, userRepo.findByUsername(username));
        return task.getComment();
    }

    public Comment addCommentOnTask(Long id, Comment comment, String username){
        Task task = taskRepo.findByIdAndCreatedBy(id, userRepo.findByUsername(username));
        comment.setTask(task);
        task.getComment().add(comment);
        return commentRepo.save(comment);
    }

}
