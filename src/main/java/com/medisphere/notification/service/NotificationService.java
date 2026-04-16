package com.medisphere.notification.service;

import com.medisphere.notification.dto.NotificationRequest;
import com.medisphere.notification.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    Notification createNotification(NotificationRequest notificationRequest);
    Notification getNotificationById(UUID id);
    List<Notification> getNotificationsByUserId(String userId);
    List<Notification> getNotificationsByRole(String role);
    List<Notification> getAllNotifications();
    Notification markAsSent(UUID id);
    Notification markAsFailed(UUID id);
    Notification broadcastNotification(NotificationRequest notificationRequest);
}
