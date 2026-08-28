package com.family_management_system.notification_service.controller;

import com.family_management_system.notification_service.dto.ApiResponse;
import com.family_management_system.notification_service.dto.NotificationResponse;
import com.family_management_system.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> notifications(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        ApiResponse<Page<NotificationResponse>> response = new ApiResponse(
                "notifications fetched",
                HttpStatus.OK,
                LocalDateTime.now(),
                notificationService.getAllNotifications(userId,page,size)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-5")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getTop5notifications(
            @RequestParam Long userId
    ){
        ApiResponse<List<NotificationResponse>> response = new ApiResponse(
                "Top 5 notifications fetched",
                HttpStatus.OK,
                LocalDateTime.now(),
                notificationService.getTop5Notifications(userId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<ApiResponse<String>> markAsRead(
            @RequestParam Long userId,
            @PathVariable Long notificationId
    ){
        ApiResponse<String> response = new ApiResponse(
                "marked notification as read",
                HttpStatus.OK,
                LocalDateTime.now(),
                notificationService.markAsRead(userId,notificationId)
        );
        return ResponseEntity.ok(response);
    }
}
