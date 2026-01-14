package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.ProjectDTO.ProjectRequest;
import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.ProjectMember;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.repo.MemberRepo;
import com.saihoz.task_app.repo.ProjectRepo;
import com.saihoz.task_app.repo.TaskRepo;
import com.saihoz.task_app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo repo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private MemberRepo memberRepo;

    public List<Project> getAllProjects(String username){
        User user = userRepo.findByUsername(username);
        return repo.findByCreatedBy(user);
    }

    public Project getProject(Long id, String username){
        User user = userRepo.findByUsername(username);
        return repo.findByIdAndCreatedBy(id, user);
    }

    public Project addProject(ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setStatus(request.status());
        project.setCreatedBy(user);
        return repo.save(project);
    }

    public Project updateProject(Long id, ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setStatus(request.status());
        return project;
    }

    public void deleteProject(Long id, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        repo.delete(project);
    }

    public List<Task> getTasksOnProject(Long id, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        List<Task> tasks = taskRepo.findByProject(project);
        return tasks;
    }

    public List<ProjectMember> getMembersOnProject(Long id, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        List<ProjectMember> members = memberRepo.findByProject(project);
        return members;
    }

    public ProjectMember addMemberOnProject(Long id, ProjectMember member, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        member.setProject(project);
        member.setUser(user);
        return memberRepo.save(member);
    }

    public void deleteMemberOnProject(Long projectId, Long memberId, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(projectId, user);
        ProjectMember member = memberRepo.findByIdAndProject(memberId, project);
        memberRepo.delete(member);
    }
}
