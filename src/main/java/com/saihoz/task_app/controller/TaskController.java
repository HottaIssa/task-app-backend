package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.KanbanDTO.TaskCardResponse;
import com.saihoz.task_app.dto.TaskDTO.PatchTaskStatusRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskUpdateRequest;
import com.saihoz.task_app.model.Comment;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<TaskCardResponse>> getAllTasks(@RequestParam(required = false) Long projectId,
                                                                @RequestParam(required = false) String status,
                                                                @RequestParam(required = false) String priority,
                                                                @RequestParam(required = false) String search,
                                                                @RequestParam(required = false) LocalDateTime dueFrom,
                                                                @RequestParam(required = false) LocalDateTime dueTo,
                                                                @RequestParam(required = false) Boolean isOverdue,
                                                                @RequestParam(required = false) Boolean isAssignedToMe,
                                                                @RequestParam(required = false) Boolean isCreatedByMe,
                                                                @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetails user){
        String username = user.getUsername();
        PagedModel<TaskCardResponse> tasks = taskService.findTasksWithFilter(projectId, status, priority, search,
                dueFrom, dueTo, isOverdue, isAssignedToMe, isCreatedByMe,username, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id){
        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> addTaskOnProject(@Valid @RequestBody TaskRequest task, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.addTask(task, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskUpdateRequest task, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.updateTask(id, task, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID id, @AuthenticationPrincipal UserDetails user){
        taskService.deleteTask(id, user.getUsername());
        return ResponseEntity.ok().body("Task deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable UUID id, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.patchTaskStatus(id, user.getUsername()));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getCommentsOnTask(@PathVariable UUID id){
        return ResponseEntity.ok().body(taskService.getCommentsOnTask(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addCommentOnTask(@PathVariable UUID id, @Valid @RequestBody Comment comment, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(taskService.addCommentOnTask(id, comment, user.getUsername()));
    }

}
