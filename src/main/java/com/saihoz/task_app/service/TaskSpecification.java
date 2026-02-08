package com.saihoz.task_app.service;

import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.model.TaskStatus;
import com.saihoz.task_app.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecification {

    public static Specification<Task> isOwnerOrAssignee(User user) {
        return (root, query, cb) -> {
            Join<Object, Object> assignedToJoin = root.join("assignedTo", JoinType.LEFT);

            return cb.or(
                    cb.equal(root.get("createdBy"), user),
                    cb.equal(assignedToJoin.get("user"), user)
            );
        };
    }

    public static Specification<Task> projectId(Long projectId) {
        return (root, query, cb) ->
                projectId == null ? null :
                        cb.equal(root.get("project").get("id"), projectId);
    }

    public static Specification<Task> status(TaskStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<Task> priority(String priority) {
        return (root, query, cb) ->
                priority == null ? null :
                        cb.equal(cb.lower(root.get("priority")), priority.toLowerCase());
    }

    public static Specification<Task> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            String like = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };
    }

    public static Specification<Task> dueBetween(
            LocalDateTime dueFrom,
            LocalDateTime dueTo) {

        return (root, query, cb) -> {
            if (dueFrom == null && dueTo == null) return null;
            if (dueFrom != null && dueTo != null)
                return cb.between(root.get("dueDate"), dueFrom, dueTo);
            if (dueFrom != null)
                return cb.greaterThanOrEqualTo(root.get("dueDate"), dueFrom);
            return cb.lessThanOrEqualTo(root.get("dueDate"), dueTo);
        };
    }

    public static Specification<Task> isOverdue(Boolean isOverdue) {
        return (root, query, cb) -> {
            if (isOverdue == null) return null;

            LocalDateTime now = LocalDateTime.now();

            return isOverdue
                    ? cb.lessThan(root.get("dueDate"), now)
                    : cb.greaterThanOrEqualTo(root.get("dueDate"), now);
        };
    }

    public static Specification<Task> isAssignedToMe(Boolean isAssignedTo, User user) {
        return (root, query, cb) -> {
            if (isAssignedTo == null || isAssignedTo == false) return null;

            return cb.equal(root.get("assignedTo").get("user"), user);
        };
    }

    public static Specification<Task> isCreatedByMe(Boolean isCreatedByMe, User user) {
        return (root, query, cb) -> {
            if (isCreatedByMe == null || isCreatedByMe == false) return null;

            return cb.equal(root.get("createdBy"), user);
        };
    }
}
