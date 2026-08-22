package com.family_management_system.notification_service.service.impl;

import com.family_management_system.notification_service.dto.NotificationResponse;
import com.family_management_system.notification_service.repository.NotificationRepository;
import com.family_management_system.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public NotificationResponse getAllNotifications() {
        return notificationRepository.;
    }
}
