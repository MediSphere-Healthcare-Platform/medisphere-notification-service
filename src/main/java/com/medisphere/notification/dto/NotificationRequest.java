package com.medisphere.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String userId;
    private String userRole;
    private String message;
    private String title;
    private String channel;
    private String relatedId;
    private boolean isBroadcast;
}
