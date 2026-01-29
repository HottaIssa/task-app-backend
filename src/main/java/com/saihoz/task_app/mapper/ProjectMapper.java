package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.ProjectDTO.ProjectRequest;
import com.saihoz.task_app.dto.ProjectDTO.ProjectResponse;
import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.ProjectStatus;
import com.saihoz.task_app.model.User;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    private final UserMapper userMapper;

    public ProjectMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ProjectResponse toResponse(Project project){
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus().getDisplayName(),
                project.getStartDate(),
                project.getEndDate(),
                userMapper.toSimpleResponse(project.getCreatedBy())
        );
    }

    public Project toEntity(ProjectRequest request, User user){
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        if(request.status() == null){
            project.setStatus(ProjectStatus.ACTIVE);
        }else {
            project.setStatus(request.status());
        }
        project.setCreatedBy(user);

        return project;
    }

}
