package com.family_management_system.notification_service.service.impl;

import com.family_management_system.notification_service.dto.NotificationResponse;
import com.family_management_system.notification_service.entity.Notification;
import com.family_management_system.notification_service.repository.NotificationRepository;
import com.family_management_system.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public Page<NotificationResponse> getAllNotifications(Long userId,int page,int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("timeStamp").descending());
        Page<Notification> notifications = notificationRepository.findByUserId(userId,pageable);
        return notifications.map(n->new NotificationResponse(
                n.getTitle(),
                n.getMessage(),
                n.isMarkAsRead(),
                n.getTimeStamp()
        ));
    }

    @Override
    public List<NotificationResponse> getTop5Notifications(Long userId) {
        List<Notification> notifications = notificationRepository.findTop5ByUserIdOrderByTimeStampDesc(userId);
        return notifications.stream().map(n->new NotificationResponse(
                n.getTitle(),
                n.getMessage(),
                n.isMarkAsRead(),
                n.getTimeStamp()
        )).toList();
    }

    @Override
    public String markAsRead(Long userId,Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()->new RuntimeException("Notification not found"));
        notification.setMarkAsRead(true);
        notificationRepository.save(notification);
        return "Notification marked as read";
    }

    @Override
    public NotificationResponse generateNotification(Long userId){
        return new NotificationResponse(
                "Notification",
                "Notification generated",
                false,
                LocalDateTime.now()
        );
    }
}
