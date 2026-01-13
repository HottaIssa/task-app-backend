package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.ProjectDTO.ProjectRequest;
import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.ProjectMember;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin("*")
public class ProjectController {

    @Autowired
    private ProjectService service;

    public ResponseEntity<List<Project>> getAllProjects(){
        return ResponseEntity.ok().body(service.getAllProjects());
    }

    public ResponseEntity<Project> getProject(Long id){
        return ResponseEntity.ok().body(service.getProject(id));
    }

    public ResponseEntity<Project> addProject(ProjectRequest request){
        return ResponseEntity.ok().body(service.addProject(request));
    }

    public ResponseEntity<String> deleteProject(@PathVariable Long id){
        service.deleteProject(id);
        return ResponseEntity.ok().body("Project deleted successfully");
    }

    public ResponseEntity<Project> updateProject(@PathVariable Long id, ProjectRequest request){
        return ResponseEntity.ok().body(service.updateProject(id, request));
    }

    public ResponseEntity<ProjectMember> addMemberOnProject(@PathVariable Long id, ProjectMember member){
        return ResponseEntity.ok().body(service.addMemberOnProject(id, member));
    }

    public ResponseEntity<String> deleteMemberOnProject(@PathVariable Long id, ProjectMember member){
        service.deleteMemberOnProject(id, member.getId());
        return ResponseEntity.ok().body("Member deleted successfully");
    }

    public ResponseEntity<List<ProjectMember>> getMembersOnProject(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getMembersOnProject(id));
    }

    public ResponseEntity<List<Task>> getTasksOnProject(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getTasksOnProject(id));
    }

}
