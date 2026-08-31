package com.family_management_system.notification_service.service.impl;

import com.family_management_system.notification_service.controller.UserClient;
import com.family_management_system.notification_service.dto.NotificationResponse;
import com.family_management_system.notification_service.entity.Notification;
import com.family_management_system.notification_service.entity.User;
import com.family_management_system.notification_service.repository.NotificationRepository;
import com.family_management_system.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserClient userClient;
    private final CacheManager cacheManager;
    @Override
    @Cacheable(value = "notifications",key = "#userId + ':' + #page + ':' + #size")
    public Page<NotificationResponse> getAllNotifications(Long userId,int page,int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("timeStamp").descending());
        Page<Notification> notifications = notificationRepository.findByUserId(userId,pageable);
        return notifications.map(n->new NotificationResponse(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.isMarkAsRead(),
                n.getTimeStamp()
        ));
    }

    @Override
    @Cacheable(value = "top5notifications",key = "#userId")
    public List<NotificationResponse> getTop5Notifications(Long userId) {
        List<Notification> notifications = notificationRepository.findTop5ByUserIdOrderByTimeStampDesc(userId);
        return notifications.stream().map(n->new NotificationResponse(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.isMarkAsRead(),
                n.getTimeStamp()
        )).toList();
    }

    @Override
    @CacheEvict(value = {"notifications", "top5notifications"}, allEntries = true)
    public String markAsRead(Long userId,Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()->new RuntimeException("Notification not found"));
        if (notification.getUser() == null || !userId.equals(notification.getUser().getId())) {
            throw new RuntimeException("Notification not found");
        }
        notification.setMarkAsRead(true);
        notificationRepository.save(notification);
        return "Notification marked as read";
    }

    @Override
    @CacheEvict(value = {"notifications", "top5notifications"}, allEntries = true)
    public String markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndMarkAsReadFalse(userId);
        for (Notification notification : unread) {
            notification.setMarkAsRead(true);
        }
        if (!unread.isEmpty()) {
            notificationRepository.saveAll(unread);
        }
        return "All notifications marked as read";
    }

    @KafkaListener(topics = {
            "family-registered",
            "family-members-added",
            "family-updated",
            "profile-updated",
            "password-changed",
            "status-changed"
    })
    public void generateNotification(
            @Payload String data,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String userId
    ){
        Notification notification = new Notification();
        notification.setTitle(topic);
        notification.setMessage(data);
        notification.setMarkAsRead(false);
        notification.setTimeStamp(LocalDateTime.now());

        if (userId != null) {
            User user = userClient.getUserById(Long.valueOf(userId));
            user.setId(Long.valueOf(userId));
            notification.setUser(user);
        }

        notificationRepository.save(notification);
        evictNotificationCaches();
    }

    private void evictNotificationCaches() {
        if (cacheManager.getCache("notifications") != null) {
            cacheManager.getCache("notifications").clear();
        }
        if (cacheManager.getCache("top5notifications") != null) {
            cacheManager.getCache("top5notifications").clear();
        }
    }
}
