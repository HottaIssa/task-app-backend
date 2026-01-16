package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.ProjectMember;
import com.saihoz.task_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepo extends JpaRepository<ProjectMember, UUID> {
    List<ProjectMember> findByProject(Project project);
    ProjectMember findByIdAndProject(UUID id, Project project);
}
