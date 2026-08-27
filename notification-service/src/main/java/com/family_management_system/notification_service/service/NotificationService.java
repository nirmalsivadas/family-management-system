package com.family_management_system.notification_service.service;

import com.family_management_system.notification_service.dto.NotificationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {

    Page<NotificationResponse> getAllNotifications(Long userId, int page, int size);

    List<NotificationResponse> getTop5Notifications(Long userId);

    String markAsRead(Long userId,Long notificationId);

}
