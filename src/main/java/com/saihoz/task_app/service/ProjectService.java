package com.saihoz.task_app.service;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.repo.ProjectRepo;
import com.saihoz.task_app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo repo;

    @Autowired
    private UserRepo userRepo;

    public List<Project> getAllProjects(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        return repo.findByUser(user);
    }

    public Project getProject(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        return repo.findByIdAndUser(id, user);
    }
}
