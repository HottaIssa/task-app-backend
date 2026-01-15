package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepo extends JpaRepository<TaskStatus, Long> {
}
