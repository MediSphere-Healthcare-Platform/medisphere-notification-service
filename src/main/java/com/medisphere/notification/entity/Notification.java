package com.medisphere.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medisphere_notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id", updatable = false, nullable = false)
    private UUID notificationId;

    @Column(name = "user_id", length = 50)
    private String userId; // NULL if broadcast

    @Column(name = "user_role", length = 20, nullable = false)
    private String userRole; // PATIENT / DOCTOR / ADMIN

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(length = 255)
    private String title;

    @Column(length = 20, nullable = false)
    private String channel; // EMAIL / SMS

    @Column(length = 20)
    private String status = "PENDING"; // PENDING / SENT / FAILED

    @Column(name = "related_id", length = 50)
    private String relatedId; // e.g., appointment_id

    @Column(name = "is_broadcast")
    private boolean isBroadcast = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
