package com.saihoz.task_app.controller;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin("*")
public class ProjectController {

    @Autowired
    private ProjectService service;

    public ResponseEntity<List<Project>> getAllProjects(){
        return ResponseEntity.ok().body(service.getAllProjects());
    }

}
