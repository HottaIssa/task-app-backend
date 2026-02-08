package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.ProjectDTO.ProjectSimpleResponse;
import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberResponse;
import com.saihoz.task_app.dto.ProjectMemberDTO.ProjectMemberSimpleResponse;
import com.saihoz.task_app.dto.UserDTO.GuestResponse;
import com.saihoz.task_app.model.*;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {

    private final UserMapper userMapper;

    public ProjectMemberMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ProjectMemberResponse toResponse(ProjectMember projectMember) {
        if (projectMember == null) return null;
        return new ProjectMemberResponse(
                projectMember.getId(),
                userMapper.toSimpleResponse(projectMember.getUser()),
                projectMember.getRoleMember().getDisplayName(),
                projectMember.getJoinedAt(),
                projectMember.getIsActive()
        );

    }

    public ProjectMember toEntity(User member, Project project, RoleMember role, User user) {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setUser(member);
        projectMember.setRoleMember(role);
        projectMember.setProject(project);
        projectMember.setInvitedBy(user);
        return projectMember;
    }

    public ProjectMemberSimpleResponse toSimpleResponse(ProjectMember member) {
        return new ProjectMemberSimpleResponse(
                member.getId(),
                member.getUser().getUsername(),
                member.getUser().getFullName(),
                member.getUser().getEmail(),
                member.getUser().getAvatar_url()
        );
    }


    public GuestResponse toGuestResponse(ProjectMember member) {
        return new GuestResponse(
                member.getId(),
                userMapper.toSimpleResponse(
                        member.getUser()
                ),
                new ProjectSimpleResponse(
                        member.getProject().getId(),
                        member.getProject().getName()
                ),
                member.getJoinedAt()

        );
    }
}
