package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.TaskDTO.TaskRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.model.Comment;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin("*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(@RequestParam(required = false) Long projectId,
                                                  @RequestParam(required = false) String status,
                                                  @RequestParam(required = false) String priority,
                                                  @RequestParam(required = false) String search,
                                                  @RequestParam(required = false) LocalDateTime dueFrom,
                                                  @RequestParam(required = false) LocalDateTime dueTo,
                                                  @RequestParam(required = false) Boolean isOverdue,
                                                  @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetails user){
        String username = user.getUsername();
        Page<TaskResponse> tasks = taskService.findTasksWithFilter(projectId, status, priority, search,
                dueFrom, dueTo, isOverdue, username, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.getTask(id, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> addTaskOnProject(@Valid @RequestBody TaskRequest task, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.addTask(task, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody Task task, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.updateTask(id, task, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        taskService.deleteTask(id, user.getUsername());
        return ResponseEntity.ok().body("Task deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long id, @RequestBody Long statusId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.patchTaskStatus(id, statusId, user.getUsername()));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getCommentsOnTask(@PathVariable Long id){
        return ResponseEntity.ok().body(taskService.getCommentsOnTask(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addCommentOnTask(@PathVariable Long id, @Valid @RequestBody Comment comment, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.addCommentOnTask(id, comment, user.getUsername()));
    }

}
