package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.KanbanDTO.KanbanBoardResponse;
import com.saihoz.task_app.dto.KanbanDTO.KanbanColumnResponse;
import com.saihoz.task_app.dto.KanbanDTO.TaskCardResponse;
import com.saihoz.task_app.dto.ProjectDTO.*;
import com.saihoz.task_app.dto.ProjectMemberDTO.PatchRoleMemberRequest;
import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberRequest;
import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberResponse;
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

    public List<ProjectSimpleResponse> getAllProjects(String username){
        User user = userRepo.findByUsername(username);
        List<Project> projects = projectRepo.findByMembers(user).stream()
                .filter(project -> project.isMember(user))
                .toList();
        return projects.stream()
                .map(project -> projectMapper.toSimpleResponse(project))
                .collect(Collectors.toList());
    }

    public List<ProjectSimpleResponse> getProjectsByStatus(String username, ProjectStatus status){
        User user = userRepo.findByUsername(username);
        List<Project> projects = projectRepo.findMyProjectsByStatus(user, status).stream()
                .filter(project -> project.isMember(user))
                .toList();
        return projects.stream()
                .map(project -> projectMapper.toSimpleResponse(project))
                .collect(Collectors.toList());
    }

    public ProjectResponse getProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        return projectMapper.toResponse(projectRepo.findByIdAndMember(id, user));
    }

    public ProjectResponse addProject(ProjectRequest request, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectMapper.toEntity(request, user);
        ProjectMember projectMember = projectMemberMapper.toEntity(user, project, RoleMember.OWNER, null);
        System.out.println(project.getName());
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
        if (!project.getCreatedBy().equals(user)) {
            throw new RuntimeException("Only admins can edit this project");
        }

        if (request.name() != null && !request.name().isBlank()) {
            project.setName(request.name());
        }

        if (request.description() != null) {
            project.setDescription(request.description());
        }

        if (request.status() != null && project.isOwner(user)) {
            project.setStatus(request.status());
        }

        if (request.startDate() != null) {
            project.setStartDate(request.startDate());
        }

        if (request.endDate() != null) {
            if (project.getStartDate() != null && request.endDate().isBefore(project.getStartDate())) {
                throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
            }
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

        ProjectMember member = memberRepo.findByUserAndProjectAndIsActive(currentUser, project, true);

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
                        project.getStatus(),
                        project.getTotalTasks(),
                        (int) project.getCompletedTasksCount(),
                        member.getRoleMember());

        // Retornar el tablero completo
            return new KanbanBoardResponse(columns.values().stream().toList(), projectSummary);
    }

    public List<ProjectMemberResponse> getMembersOnProject(UUID id, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(id, user);
        List<ProjectMember> members = memberRepo.findByProjectOrderByJoinedAtAsc(project);
        return members.stream()
                .map(projectMemberMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectMemberResponse addMemberOnProject(UUID id, ProjectMemberRequest member, String username){
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findByIdAndMember(id, user);
        User userMember = userRepo.findById(member.userId()).orElseThrow(() -> new RuntimeException("User not found"));
        if(project.isMember(userMember)) throw new RuntimeException("This User is already a member");
        ProjectMember projectMember = projectMemberMapper.toEntity(userMember, project, member.role(), user);
        memberRepo.save(projectMember);
        return projectMemberMapper.toResponse(projectMember);
    }

    public ProjectMemberResponse patchRoleMember(UUID projectId, UUID memberId , PatchRoleMemberRequest request, String username) {
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findById(projectId).orElse(null);
        if(!project.isAdmin(user)) throw new RuntimeException("No tienes el nivel requerido para realizar esta acción");
        ProjectMember member = memberRepo.findById(memberId).orElse(null);
        member.setRoleMember(request.role());
        return projectMemberMapper.toResponse(memberRepo.save(member));
    }

    public ProjectMemberResponse patchDeactivateMember(UUID projectId,UUID memberId, String username) {
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findById(projectId).orElse(null);
        if(!project.isAdmin(user)) throw new RuntimeException("No tienes el nivel requerido para realizar esta acción");
        ProjectMember member = memberRepo.findById(memberId).orElse(null);
        member.setIsActive(false);
        return projectMemberMapper.toResponse(memberRepo.save(member));
    }

    public ProjectMemberResponse patchActivateMember(UUID projectId,UUID memberId, String username) {
        User user = userRepo.findByUsername(username);
        Project project = projectRepo.findById(projectId).orElse(null);
        if(!project.isAdmin(user)) throw new RuntimeException("No tienes el nivel requerido para realizar esta acción");
        ProjectMember member = memberRepo.findById(memberId).orElse(null);
        member.setIsActive(true);
        return projectMemberMapper.toResponse(memberRepo.save(member));
    }


    public ProjectDashboardResponse getDashboardProjects(String username) {
        User user = userRepo.findByUsername(username);

        List<ProjectStatus> dashboardStatuses = List.of(
                ProjectStatus.ACTIVE,
                ProjectStatus.ON_HOLD,
                ProjectStatus.COMPLETED
        );

        List<Project> projects = projectRepo.findMyProjectsByStatusIn(user, dashboardStatuses);

        List<ProjectResponse> active = new ArrayList<>();
        List<ProjectResponse> onHold = new ArrayList<>();
        List<ProjectResponse> completed = new ArrayList<>();

        for (Project p : projects) {
            var mapped = projectMapper.toResponse(p);
            switch (p.getStatus()) {
                case ACTIVE -> active.add(mapped);
                case ON_HOLD -> onHold.add(mapped);
                case COMPLETED -> completed.add(mapped);
            }
        }

        return new ProjectDashboardResponse(active, onHold, completed);
    }

    public ProjectHistoryResponse getHistoryProjects(String username){
        User user = userRepo.findByUsername(username);

        List<ProjectStatus> historyStatuses = List.of(
                ProjectStatus.COMPLETED,
                ProjectStatus.CANCELLED
        );

        List<Project> projects = projectRepo.findMyProjectsByStatusIn(user, historyStatuses);

        List<ProjectResponse> completed = new ArrayList<>();
        List<ProjectResponse> cancelled = new ArrayList<>();

        for (Project p : projects) {
            var mapped = projectMapper.toResponse(p);
            switch (p.getStatus()) {
                case COMPLETED -> completed.add(mapped);
                case CANCELLED -> cancelled.add(mapped);
            }
        }

        return new ProjectHistoryResponse(completed, cancelled);
    }
}
