package com.saihoz.task_app.repo;

import com.saihoz.task_app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
