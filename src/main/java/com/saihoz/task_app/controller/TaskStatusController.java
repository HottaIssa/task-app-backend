package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.TaskDTO.TaskStatusRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskStatusResponse;
import com.saihoz.task_app.model.TaskStatus;
import com.saihoz.task_app.service.TaskStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-status")
public class TaskStatusController {

    @Autowired
    private TaskStatusService service;

    @GetMapping
    public ResponseEntity<List<TaskStatusResponse>> getAllTaskStatus(){
        return ResponseEntity.ok().body(service.getAllTaskStatus());
    }

    @PostMapping
    public ResponseEntity<TaskStatusResponse> addTaskStatus(@Valid @RequestBody TaskStatusRequest taskStatus){
        return ResponseEntity.ok().body(service.addTaskStatus(taskStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskStatus(@PathVariable Long id){
        service.deleteTaskStatus(id);
        return ResponseEntity.ok().body("Task statusId deleted successfully");
    }

}
