package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepo extends JpaRepository<Comment, UUID> {
    @Query("SELECT c FROM Comment c WHERE c.task.id = :id ORDER BY c.createdAt DESC")
    List<Comment> findByTaskId(UUID id);
}
