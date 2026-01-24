package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.ProjectDTO.ProjectMemberResponse;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.model.*;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {

    public ProjectMemberResponse toResponse(ProjectMember projectMember){
        return new ProjectMemberResponse(
                projectMember.getId(),
                new UserResponse(
                        projectMember.getUser().getId(),
                        projectMember.getUser().getUsername(),
                        projectMember.getUser().getEmail(),
                        projectMember.getUser().getFirstName(),
                        projectMember.getUser().getLastName(),
                        projectMember.getUser().getRole().getDisplayName(),
                        projectMember.getUser().getAvatar_url()
                ),
                projectMember.getRoleMember().getDisplayName(),
                projectMember.getJoinedAt()
        );

    }

    public ProjectMember toEntity(User user, Project project, RoleMember role){
        ProjectMember projectMember = new ProjectMember();
        projectMember.setUser(user);
        projectMember.setRoleMember(role);
        projectMember.setProject(project);
        return projectMember;
    }
}
