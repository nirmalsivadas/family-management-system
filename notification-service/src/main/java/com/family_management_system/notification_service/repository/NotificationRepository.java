package com.family_management_system.notification_service.repository;

import com.family_management_system.notification_service.dto.NotificationResponse;
import com.family_management_system.notification_service.entity.Notification;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<NotificationResponse> PageRequest.of() findAll();
}
