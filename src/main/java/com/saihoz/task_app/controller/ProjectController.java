package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.KanbanDTO.KanbanBoardResponse;
import com.saihoz.task_app.dto.ProjectDTO.*;
import com.saihoz.task_app.dto.ProjectMemberDTO.PatchRoleMemberRequest;
import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberRequest;
import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberResponse;
import com.saihoz.task_app.model.ProjectStatus;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @GetMapping
    public ResponseEntity<List<ProjectSimpleResponse>> getProjects(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) ProjectStatus status
    ) {
        if (status != null) {
            return ResponseEntity.ok(
                    service.getProjectsByStatus(user.getUsername(), status)
            );
        }

        return ResponseEntity.ok(
                service.getAllProjects(user.getUsername())
        );
    }

    @GetMapping("dashboard")
    public ResponseEntity<ProjectDashboardResponse> getDashboard(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
                service.getDashboardProjects(user.getUsername())
        );
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

    @PatchMapping("/{projectId}/members/{memberId}/role")
    public ResponseEntity<ProjectMemberResponse> patchRoleMember(@PathVariable UUID projectId, @PathVariable UUID memberId, @RequestBody PatchRoleMemberRequest role, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.patchRoleMember(projectId, memberId, role, user.getUsername()));
    }

    @PatchMapping("/{projectId}/members/{memberId}/deactivate")
    public ResponseEntity<ProjectMemberResponse> patchDeactivateMember(@PathVariable UUID projectId, @PathVariable UUID memberId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.patchDeactivateMember(projectId, memberId, user.getUsername()));
    }

    @PatchMapping("/{projectId}/members/{memberId}/active")
    public ResponseEntity<ProjectMemberResponse> patchActivateMember(@PathVariable UUID projectId, @PathVariable UUID memberId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.patchActivateMember(projectId, memberId, user.getUsername()));
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<KanbanBoardResponse> getTasksOnProject(@PathVariable UUID projectId, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getKanbanBoard(projectId, user.getUsername()));
    }

}
