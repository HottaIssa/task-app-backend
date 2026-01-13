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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    final ProjectRepo repo;

    final UserRepo userRepo;

    final TaskRepo taskRepo;

    final MemberRepo memberRepo;

    public ProjectService(ProjectRepo repo, UserRepo userRepo, TaskRepo taskRepo, MemberRepo memberRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.memberRepo = memberRepo;
    }

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

    public Project addProject(ProjectRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
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

    public Project updateProject(Long id, ProjectRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndUser(id, user);
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setStatus(request.status());
        return project;
    }

    public void deleteProject(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndUser(id, user);
        repo.delete(project);
    }

    public List<Task> getTasksOnProject(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndUser(id, user);
        List<Task> tasks = taskRepo.findByProject(project);
        return tasks;
    }

    public List<ProjectMember> getMembersOnProject(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndUser(id, user);
        List<ProjectMember> members = memberRepo.findByProject(project);
        return members;
    }

    public ProjectMember addMemberOnProject(Long id, ProjectMember member){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndUser(id, user);
        member.setProject(project);
        member.setUser(user);
        return memberRepo.save(member);
    }

    public void deleteMemberOnProject(Long projectId, Long memberId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndUser(projectId, user);
        ProjectMember member = memberRepo.findByIdAndProject(memberId, project);
        memberRepo.delete(member);
    }
}
