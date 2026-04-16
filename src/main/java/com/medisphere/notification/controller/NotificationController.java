package com.medisphere.notification.controller;

import com.medisphere.notification.dto.NotificationRequest;
import com.medisphere.notification.dto.NotificationResponse;
import com.medisphere.notification.entity.Notification;
import com.medisphere.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest notificationRequest) {
        Notification notification = notificationService.createNotification(notificationRequest);
        return new ResponseEntity<>(new NotificationResponse(notification), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Notification>> getNotificationsByRole(@PathVariable String role) {
        return ResponseEntity.ok(notificationService.getNotificationsByRole(role));
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @PutMapping("/{id}/sent")
    public ResponseEntity<NotificationResponse> markAsSent(@PathVariable UUID id) {
        Notification notification = notificationService.markAsSent(id);
        return ResponseEntity.ok(new NotificationResponse(notification));
    }

    @PutMapping("/{id}/failed")
    public ResponseEntity<NotificationResponse> markAsFailed(@PathVariable UUID id) {
        Notification notification = notificationService.markAsFailed(id);
        return ResponseEntity.ok(new NotificationResponse(notification));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<NotificationResponse> broadcastNotification(@RequestBody NotificationRequest notificationRequest) {
        Notification notification = notificationService.broadcastNotification(notificationRequest);
        return new ResponseEntity<>(new NotificationResponse(notification), HttpStatus.CREATED);
    }
}
