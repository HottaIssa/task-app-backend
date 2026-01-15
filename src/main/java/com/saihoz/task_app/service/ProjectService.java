package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.ProjectDTO.ProjectMemberRequest;
import com.saihoz.task_app.dto.ProjectDTO.ProjectRequest;
import com.saihoz.task_app.dto.ProjectDTO.ProjectResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.mapper.ProjectMapper;
import com.saihoz.task_app.mapper.TaskMapper;
import com.saihoz.task_app.model.*;
import com.saihoz.task_app.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ProjectMapper projectMapper;

    public List<ProjectResponse> getAllProjects(String username){
        User user = userRepo.findByUsername(username);
        List<Project> projects = repo.findByCreatedBy(user);
        return projects.stream()
                .map(project -> projectMapper.toResponse(project))
                .collect(Collectors.toList());
    }

    public ProjectResponse getProject(Long id, String username){
        User user = userRepo.findByUsername(username);
        return projectMapper.toResponse(repo.findByIdAndCreatedBy(id, user));
    }

    public ProjectResponse addProject(ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectMapper.toEntity(request, user);
        return projectMapper.toResponse(repo.save(project));
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
        repo.delete(repo.findByIdAndCreatedBy(id, user));
    }

    public List<TaskResponse> getTasksOnProject(Long id, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        List<Task> tasks = taskRepo.findByProject(project);
        return tasks.stream()
                .map(task -> taskMapper.toResponse(task))
                .collect(Collectors.toList());
    }

    public List<ProjectMember> getMembersOnProject(Long id, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        return memberRepo.findByProject(project);
    }

    public ProjectMember addMemberOnProject(Long id, ProjectMemberRequest member, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        User userMember = userRepo.findById(member.userId()).orElse(null);
        Role role = roleRepo.findByName(member.role());
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(userMember);
        projectMember.setRole(role);
        return memberRepo.save(projectMember);
    }

    public void deleteMemberOnProject(Long projectId, Long memberId, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(projectId, user);
        ProjectMember member = memberRepo.findByIdAndProject(memberId, project);
        memberRepo.delete(member);
    }
}
