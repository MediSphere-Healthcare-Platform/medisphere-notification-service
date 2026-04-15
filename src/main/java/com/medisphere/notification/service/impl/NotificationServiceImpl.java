package com.medisphere.notification.service.impl;

import com.medisphere.notification.dto.NotificationRequest;
import com.medisphere.notification.entity.Notification;
import com.medisphere.notification.exception.ResourceNotFoundException;
import com.medisphere.notification.repository.NotificationRepository;
import com.medisphere.notification.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationRequest, notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
    }

    @Override
    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getNotificationsByRole(String role) {
        return notificationRepository.findByUserRole(role);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification markAsSent(UUID id) {
        Notification notification = getNotificationById(id);
        notification.setStatus("SENT");
        return notificationRepository.save(notification);
    }

    @Override
    public Notification markAsFailed(UUID id) {
        Notification notification = getNotificationById(id);
        notification.setStatus("FAILED");
        return notificationRepository.save(notification);
    }

    @Override
    public Notification broadcastNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationRequest, notification);
        notification.setBroadcast(true);
        notification.setUserId(null); // Broadcast notifications don't have a specific user
        return notificationRepository.save(notification);
    }
}
