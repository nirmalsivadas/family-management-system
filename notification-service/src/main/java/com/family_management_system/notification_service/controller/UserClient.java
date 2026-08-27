package com.family_management_system.notification_service.controller;

import com.family_management_system.notification_service.dto.UserDto;
import com.family_management_system.notification_service.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "family-service",path = "/api/users")
public interface UserClient {
    @GetMapping("/{userId}")
    User getUserById(@PathVariable Long userId);
}
