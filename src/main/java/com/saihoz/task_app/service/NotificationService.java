package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.NotificationDTO.NotificationMessage;
import com.saihoz.task_app.dto.NotificationDTO.NotificationResponse;
import com.saihoz.task_app.model.Notification;
import com.saihoz.task_app.model.NotificationType;
import com.saihoz.task_app.model.Task;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.repo.NotificationRepo;
import com.saihoz.task_app.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepo notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepo userRepository;

    @Transactional
    public void notifyTaskAssignment(User assignedUser, Task task, User assignedBy) {
        String message = String.format(
                "%s te asignó la tarea: %s",
                assignedBy.getFullName(),
                task.getTitle()
        );

        createAndSendNotification(
                assignedUser,
                task,
                NotificationType.ASSIGNED,
                message
        );
    }

    @Transactional
    public void notifyTaskCommented(User targetUser, Task task, User commentAuthor) {
        String message = String.format(
                "%s comentó en: %s",
                commentAuthor.getFullName(),
                task.getTitle()
        );

        createAndSendNotification(
                targetUser,
                task,
                NotificationType.COMMENT,
                message
        );
    }

    @Transactional
    public void notifyTaskStatusChanged(User targetUser, Task task, String newStatus, User changedBy) {
        String message = String.format(
                "%s cambió el estado de '%s' a '%s'",
                changedBy.getFullName(),
                task.getTitle(),
                newStatus
        );

        createAndSendNotification(
                targetUser,
                task,
                NotificationType.STATUS_CHANGE,
                message
        );
    }

    @Transactional
    public void notifyMemberAdded(User targetUser, String projectName) {
        String message = String.format(
                "Fuiste añadido al proyecto: %s",
                projectName
        );

        createAndSendNotification(
                targetUser,
                null,
                NotificationType.MEMBER_ADDED,
                message
        );
    }

    @Transactional
    public void notifyTaskCreated(User targetUser, Task task, User creator) {
        String message = String.format(
                "%s creó una nueva tarea: %s",
                creator.getFullName(),
                task.getTitle()
        );

        createAndSendNotification(
                targetUser,
                task,
                NotificationType.TASK_CREATED,
                message
        );
    }

    @Transactional
    protected void createAndSendNotification(
            User user,
            Task task,
            NotificationType type,
            String message
    ) {
        Notification notification = Notification.builder()
                .user(user)
                .task(task)
                .type(type)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);
        sendNotificationViaPush(saved);
    }

    private void sendNotificationViaPush(Notification notification) {

        NotificationMessage message = new NotificationMessage(
                notification.getId(),
                notification.getUser().getId(),
                notification.getTask() != null ? notification.getTask().getId() : null,
                notification.getTask() != null ? notification.getTask().getTitle() : null,
                notification.getMessage(),
                notification.getType().name(),
                notification.isRead(),
                notification.getCreatedAt(),
                LocalDateTime.now()
        );

        try {
            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + notification.getUser().getId(),
                    message
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username);
        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username);
        return notificationRepository
                .findByUserAndIsReadFalseOrderByCreatedAtDesc(user, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(String username) {
        User user = userRepository.findByUsername(username);
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Transactional
    public void markAsRead(UUID notificationId, String username) {
        User user = userRepository.findByUsername(username);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para marcar esta notificación");
        }

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(String username) {
        User user = userRepository.findByUsername(username);
        notificationRepository
                .findByUserAndIsReadFalseOrderByCreatedAtDesc(user, Pageable.unpaged())
                .forEach(notification -> {
                    notification.setRead(true);
                    notification.setReadAt(LocalDateTime.now());
                });
    }

    @Transactional
    public void deleteNotification(UUID notificationId, String username) {
        User user = userRepository.findByUsername(username);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta notificación");
        }

        notificationRepository.delete(notification);
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getType().name(),
                notification.getTask() != null ? notification.getTask().getId() : null,
                notification.getTask() != null ? notification.getTask().getTitle() : null,
                notification.getTask() != null ? notification.getTask().getProject().getId() : null,
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
