package com.family_management_system.auth_service.controller;

import com.family_management_system.auth_service.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "family-service",url = "http://localhost:8080")
public interface UserClient {
    @GetMapping
    User getUserByEmail(String email);
}
