package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.ProjectDTO.ProjectRequest;
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
    public ResponseEntity<List<Project>> getAllProjects(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getAllProjects(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getProject(id, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<Project> addProject(@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal UserDetails user){
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

    @PostMapping("/members/{id}")
    public ResponseEntity<ProjectMember> addMemberOnProject(@PathVariable Long id,@Valid @RequestBody ProjectMember member, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.addMemberOnProject(id, member, user.getUsername()));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<String> deleteMemberOnProject(@PathVariable Long id,@Valid @RequestBody ProjectMember member, @AuthenticationPrincipal UserDetails user){
        service.deleteMemberOnProject(id, member.getId(), user.getUsername());
        return ResponseEntity.ok().body("Member deleted successfully");
    }

    @GetMapping("/members")
    public ResponseEntity<List<ProjectMember>> getMembersOnProject(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getMembersOnProject(id, user.getUsername()));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksOnProject(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getTasksOnProject(id, user.getUsername()));
    }

}
