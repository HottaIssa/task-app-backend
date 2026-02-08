package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.ProjectStatus;
import com.saihoz.task_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepo extends JpaRepository<Project, UUID> {

    List<Project> findByCreatedBy(User user);

    Project findByIdAndCreatedBy(UUID id, User user);

    @Query("SELECT p FROM Project p JOIN ProjectMember m ON p = m.project WHERE m.user = :user")
    List<Project> findByMembers(User user);

    @Query("SELECT p FROM Project p JOIN ProjectMember m ON p = m.project WHERE p.id = :id AND m.user = :user")
    Project findByIdAndMember(UUID id, User user);

    @Query("SELECT p FROM Project p JOIN ProjectMember m ON p = m.project WHERE p.status = :status AND m.user = :user")
    List<Project> findMyProjectsByStatus(User user, ProjectStatus status);

    @Query("SELECT p FROM Project p JOIN ProjectMember m ON p = m.project WHERE p.status IN :dashboardStatuses AND m.user = :user")
    List<Project> findMyProjectsByStatusIn(User user, List<ProjectStatus> dashboardStatuses);
}
