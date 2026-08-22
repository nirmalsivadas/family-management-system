package com.family_management_system.notification_service.entity;

import java.time.LocalDateTime;

@Entity
@Ta
public class Notification {

    @Id
    private Long id;

    private String title;
    private String message;
    private boolean markAsRead;
    private LocalDateTime timeStamp;

}
