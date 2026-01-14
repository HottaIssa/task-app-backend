package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    Page<Task> findByCreatedBy(User createdBy, Specification<Task> spec, Pageable pageable);
    Task findByIdAndCreatedBy(Long id, User user);
}
