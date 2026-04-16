package com.medisphere.notification.service.impl;

import com.medisphere.notification.client.AuthClient;
import com.medisphere.notification.dto.ApiResponse;
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

    @Autowired
    private AuthClient authClient;

    @Override
    public Notification createNotification(NotificationRequest notificationRequest) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationRequest, notification);
        
        // Logic to automatically resolve msUserId to real Email
        String targetEmail = notification.getUserId();
        if (targetEmail != null && (targetEmail.startsWith("UD") || targetEmail.startsWith("UP"))) {
            try {
                System.out.println("Wait! Receiving ID: " + targetEmail + ". Checking Auth Service for email...");
                var response = authClient.getEmailByMsUserId(targetEmail);
                if (response.getBody() != null && "SUCCESS".equals(response.getBody().getStatus())) {
                    targetEmail = response.getBody().getData();
                    System.out.println("Success! Found email: " + targetEmail);
                    // Update the notification record to store the real email
                    notification.setUserId(targetEmail);
                } else {
                    System.err.println("Failed to resolve ID " + targetEmail + ": " + 
                        (response.getBody() != null ? response.getBody().getMessage() : "User not found"));
                }
            } catch (Exception e) {
                System.err.println("Auth Service lookup failed: " + e.getMessage());
            }
        }
        
        Notification savedNotification = notificationRepository.save(notification);
        System.out.println("Notification record created for: " + notification.getUserId());
        
        // Send actual email ONLY if we have a valid email address (contains @)
        if (targetEmail != null && targetEmail.contains("@")) {
            sendEmail(targetEmail, notification.getTitle(), notification.getMessage());
        } else {
            System.err.println("Aborting email send: Address '" + targetEmail + "' is not a valid email. " +
                "This usually means the ID lookup failed or Eureka is still syncing.");
        }
        
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
