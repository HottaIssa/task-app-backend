package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.ProjectDTO.ProjectMemberRequest;
import com.saihoz.task_app.dto.ProjectDTO.ProjectRequest;
import com.saihoz.task_app.dto.ProjectDTO.ProjectResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.ProjectMember;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin("*")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getAllProjects(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getProject(id, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> addProject(@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal UserDetails user){
        return new ResponseEntity<>(service.addProject(request, user.getUsername()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        service.deleteProject(id, user.getUsername());
        return ResponseEntity.ok().body("Project deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.updateProject(id, request, user.getUsername()));
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMember>> getMembersOnProject(@PathVariable Long projectId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getMembersOnProject(projectId, user.getUsername()));
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectMember> addMemberOnProject(@PathVariable Long projectId, @Valid @RequestBody ProjectMemberRequest member, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.addMemberOnProject(projectId, member, user.getUsername()));
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<String> deleteMemberOnProject(@PathVariable Long projectId, @PathVariable Long memberId, @AuthenticationPrincipal UserDetails user){
        service.deleteMemberOnProject(projectId, memberId, user.getUsername());
        return ResponseEntity.ok().body("Member deleted successfully");
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasksOnProject(@PathVariable Long projectId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getTasksOnProject(projectId, user.getUsername()));
    }

}
