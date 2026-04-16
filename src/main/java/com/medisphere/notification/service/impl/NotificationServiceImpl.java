package com.medisphere.notification.service.impl;

import com.medisphere.notification.dto.NotificationRequest;
import com.medisphere.notification.entity.Notification;
import com.medisphere.notification.exception.ResourceNotFoundException;
import com.medisphere.notification.repository.NotificationRepository;
import com.medisphere.notification.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Notification createNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationRequest, notification);
        
        Notification savedNotification = notificationRepository.save(notification);
        System.out.println("Notification saved to DB for: " + notification.getUserId());
        
        // Send Email
        System.out.println("Attempting to send email to " + notification.getUserId() + "...");
        sendEmail(notification.getUserId(), notification.getTitle(), notification.getMessage());
        
        return savedNotification;
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
        notification.setUserId(null);
        return notificationRepository.save(notification);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("bawantha2819@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
