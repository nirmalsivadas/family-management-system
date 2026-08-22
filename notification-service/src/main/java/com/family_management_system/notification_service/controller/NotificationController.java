package com.family_management_system.notification_service.controller;

import com.family_management_system.notification_service.dto.NotificationResponse;
import com.family_management_system.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> notifications(){
        ApiResponse<NotificationResponse> response = new ApiResponse(
                "notifications fetched",
                HttpStatus.OK,
                LocalDateTime.now(),
                notificationService.getAllNotifications()
        );
        return ResponseEntity.ok(response);
    }
}
