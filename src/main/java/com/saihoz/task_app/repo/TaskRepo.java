package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
}
