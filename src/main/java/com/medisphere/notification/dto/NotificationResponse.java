package com.medisphere.notification.dto;

import com.medisphere.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private UUID notificationId;
    private String message;
    private String status;
    private LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.message = notification.getMessage();
        this.status = notification.getStatus();
        this.createdAt = notification.getCreatedAt();
    }
}
