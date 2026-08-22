package com.family_management_system.notification_service.dto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String title;
    private String message;
    private boolean markAsRead;
    private LocalDateTime timeStamp;
}
