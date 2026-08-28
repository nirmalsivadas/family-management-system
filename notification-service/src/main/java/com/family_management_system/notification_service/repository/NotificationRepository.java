package com.family_management_system.notification_service.repository;
import com.family_management_system.notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    List<Notification> findTop5ByUserIdOrderByTimeStampDesc(Long userId);
    List<Notification> findByUserIdAndMarkAsReadFalse(Long userId);
}
