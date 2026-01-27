package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.KanbanDTO.KanbanBoardResponse;
import com.saihoz.task_app.dto.KanbanDTO.KanbanColumnResponse;
import com.saihoz.task_app.dto.KanbanDTO.TaskCardResponse;
import com.saihoz.task_app.dto.ProjectDTO.*;
import com.saihoz.task_app.dto.TaskDTO.TaskListResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskStatusResponse;
import com.saihoz.task_app.mapper.ProjectMapper;
import com.saihoz.task_app.mapper.ProjectMemberMapper;
import com.saihoz.task_app.mapper.TaskMapper;
import com.saihoz.task_app.model.*;
import com.saihoz.task_app.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

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

    @Autowired
    private TaskStatusRepo taskStatusRepo;

    public List<ProjectResponse> getAllProjects(String username){
        User user = userRepo.findByUsername(username);
        List<Project> projects = projectRepo.findByMembers(user);
        return projects.stream()
                .map(project -> projectMapper.toResponse(project))
                .collect(Collectors.toList());
    }

    public ProjectResponse getProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        return projectMapper.toResponse(projectRepo.findByIdAndMember(id, user));
    }

    public ProjectResponse addProject(ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectMapper.toEntity(request, user);
        ProjectMember projectMember = projectMemberMapper.toEntity(user, project, RoleMember.ADMIN);
        ProjectResponse projectSaved = projectMapper.toResponse(projectRepo.save(project));
        memberRepo.save(projectMember);
        return projectSaved;
    }

    public ProjectResponse updateProject(UUID id, ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(id, user);
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        project.setStatus(request.status());
        Project updatedProject = projectRepo.save(project);
        return projectMapper.toResponse(updatedProject);
    }

    public ProjectResponse patchProject(UUID id, ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(id, user);
        if (request.name() != null) {
            project.setName(request.name());
        }
        if (request.description() != null) {
            project.setDescription(request.description());
        }
        if (request.status() != null) {
            project.setStatus(request.status());
        }
        if (request.endDate() != null) {
            project.setEndDate(request.endDate());
        }
        Project updatedProject = projectRepo.save(project);
        return projectMapper.toResponse(updatedProject);
    }

    public void deleteProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(id, user);
        ProjectMember member = memberRepo.findByUserAndProject(user, project);
        if(!member.hasAdminPermissions()){
            projectRepo.delete(project);
        }
    }

    public KanbanBoardResponse getKanbanBoard(UUID projectId, String username) {
        User currentUser = userRepo.findByUsername(username);

        Project project = projectRepo.findById(projectId).orElse(null);

        ProjectMember member = memberRepo.findByUserAndProject(currentUser, project);

        List<TaskStatus> allStatuses = taskStatusRepo
                .findByProjectOrProjectIsNullOrderByOrderIndex(project);

        List<Task> tasks = taskRepo.findByProjectOrderByCreatedAtDesc(project);

        Map<Long, List<Task>> tasksByStatusId = tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getStatus().getId()));

        Map<String, KanbanColumnResponse> columns = new LinkedHashMap<>();

        for (TaskStatus status : allStatuses) {
            List<Task> statusTasks = tasksByStatusId.getOrDefault(status.getId(), new ArrayList<>());

            List<TaskCardResponse> taskCards = statusTasks.stream()
                    .map(taskMapper::toTaskCardResponse)
                    .collect(Collectors.toList());

            KanbanColumnResponse column = new KanbanColumnResponse(
                    status.getId(),
                    status.getName(),
                    status.getColor(),
                    status.getOrderIndex(),
                    taskCards.size(),
                    taskCards);

            columns.put(status.getName(), column);
        }

        ProjectSummaryResponse projectSummary = new ProjectSummaryResponse(
                        project.getId(),
                        project.getName(),
                        project.getTotalTasks(),
                        (int) project.getCompletedTasksCount(),
                        member.getRoleMember());

        // Retornar el tablero completo
            return new KanbanBoardResponse(columns.values().stream().toList(), projectSummary);
    }

    public List<ProjectMemberResponse> getMembersOnProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(id, user);
        List<ProjectMember> members = memberRepo.findByProject(project);
        return members.stream()
                .map(projectMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectMemberResponse addMemberOnProject(UUID id, ProjectMemberRequest member, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(id, user);
        User userMember = userRepo.findById(member.userId()).orElse(null);
        ProjectMember projectMember = projectMemberMapper.toEntity(userMember, project, member.role());
        memberRepo.save(projectMember);
        return projectMemberMapper.toResponse(projectMember);
    }

    public void deleteMemberOnProject(UUID projectId, UUID memberId, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(projectId, user);
        ProjectMember member = memberRepo.findByUserAndProject(user, project);
        memberRepo.delete(member);
    }
}
