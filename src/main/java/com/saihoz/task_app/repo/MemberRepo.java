package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.ProjectMember;
import com.saihoz.task_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepo extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);
    ProjectMember findByIdAndProject(Long id, Project project);
}
