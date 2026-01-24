package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Project;
import com.saihoz.task_app.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStatusRepo extends JpaRepository<TaskStatus, Long> {

    // Obtener estados del proyecto + estados globales, ordenados
    @Query("SELECT ts FROM TaskStatus ts WHERE ts.project = :project OR ts.project IS NULL ORDER BY ts.orderIndex ASC")
    List<TaskStatus> findByProjectOrProjectIsNullOrderByOrderIndex(@Param("project") Project project);

    // Solo estados globales
    List<TaskStatus> findByProjectIsNullOrderByOrderIndex();

    // Estados de un proyecto específico
    List<TaskStatus> findByProjectOrderByOrderIndex(Project project);

}
