package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.ProjectDTO.ProjectMemberRequest;
import com.saihoz.task_app.dto.ProjectDTO.ProjectMemberResponse;
import com.saihoz.task_app.dto.ProjectDTO.ProjectRequest;
import com.saihoz.task_app.dto.ProjectDTO.ProjectResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.mapper.ProjectMapper;
import com.saihoz.task_app.mapper.ProjectMemberMapper;
import com.saihoz.task_app.mapper.TaskMapper;
import com.saihoz.task_app.model.*;
import com.saihoz.task_app.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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
    private TaskMapper taskMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    public List<ProjectResponse> getAllProjects(String username){
        User user = userRepo.findByUsername(username);
        List<Project> projects = repo.findByCreatedBy(user);
        return projects.stream()
                .map(project -> projectMapper.toResponse(project))
                .collect(Collectors.toList());
    }

    public ProjectResponse getProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        return projectMapper.toResponse(repo.findByIdAndCreatedBy(id, user));
    }

    public ProjectResponse addProject(ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectMapper.toEntity(request, user);
        ProjectMember projectMember = projectMemberMapper.toEntity(user, project, RoleMember.ADMIN);
        ProjectResponse projectSaved = projectMapper.toResponse(repo.save(project));
        memberRepo.save(projectMember);
        return projectSaved;
    }

    public Project updateProject(UUID id, ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setStatus(request.status());
        return project;
    }

    public void deleteProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        repo.delete(repo.findByIdAndCreatedBy(id, user));
    }

    public List<TaskResponse> getTasksOnProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        List<Task> tasks = taskRepo.findByProject(project);
        return tasks.stream()
                .map(task -> taskMapper.toResponse(task))
                .collect(Collectors.toList());
    }

    public List<ProjectMemberResponse> getMembersOnProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        List<ProjectMember> members = memberRepo.findByProject(project);
        return members.stream()
                .map(projectMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectMemberResponse addMemberOnProject(UUID id, ProjectMemberRequest member, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(id, user);
        User userMember = userRepo.findById(member.userId()).orElse(null);
        ProjectMember projectMember = projectMemberMapper.toEntity(userMember, project, member.role());
        memberRepo.save(projectMember);
        return projectMemberMapper.toResponse(projectMember);
    }

    public void deleteMemberOnProject(UUID projectId, UUID memberId, String username){
        User user = userRepo.findByUsername(username);
        Project project = repo.findByIdAndCreatedBy(projectId, user);
        ProjectMember member = memberRepo.findByIdAndProject(memberId, project);
        memberRepo.delete(member);
    }
}
