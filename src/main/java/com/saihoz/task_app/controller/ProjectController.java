package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.KanbanDTO.KanbanBoardResponse;
import com.saihoz.task_app.dto.KanbanDTO.KanbanColumnResponse;
import com.saihoz.task_app.dto.ProjectDTO.*;
import com.saihoz.task_app.dto.TaskDTO.PatchTaskStatusRequest;
import com.saihoz.task_app.dto.TaskDTO.TaskListResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskStatusResponse;
import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getAllProjects(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable UUID id, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getProject(id, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> addProject(@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal UserDetails user){
        return new ResponseEntity<>(service.addProject(request, user.getUsername()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable UUID id, @AuthenticationPrincipal UserDetails user){
        service.deleteProject(id, user.getUsername());
        return ResponseEntity.ok().body("Project deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable UUID id,@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.updateProject(id, request, user.getUsername()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateTaskStatus(@PathVariable UUID id, @RequestBody ProjectRequest request, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.patchProject(id, request, user.getUsername()));
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberResponse>> getMembersOnProject(@PathVariable UUID projectId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getMembersOnProject(projectId, user.getUsername()));
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectMemberResponse> addMemberOnProject(@PathVariable UUID projectId, @Valid @RequestBody ProjectMemberRequest member, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.addMemberOnProject(projectId, member, user.getUsername()));
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<String> deleteMemberOnProject(@PathVariable UUID projectId, @PathVariable UUID memberId, @AuthenticationPrincipal UserDetails user){
        service.deleteMemberOnProject(projectId, memberId, user.getUsername());
        return ResponseEntity.ok().body("Member deleted successfully");
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<KanbanBoardResponse> getTasksOnProject(@PathVariable UUID projectId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getKanbanBoard(projectId, user.getUsername()));
    }

}
