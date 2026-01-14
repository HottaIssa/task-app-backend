package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    List<Project> findByCreatedBy(User user);

    Project findByIdAndCreatedBy(Long id, User user);

}
