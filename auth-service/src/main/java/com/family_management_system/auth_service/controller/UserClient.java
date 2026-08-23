package com.family_management_system.auth_service.controller;

import com.family_management_system.auth_service.dto.SignupRequest;
import com.family_management_system.auth_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "family-service",path = "/api/users")
public interface UserClient {
    @GetMapping("/by-email")
    UserDto getUserByEmail(@RequestParam String email);

    @PostMapping
    UserDto createUser(@RequestBody SignupRequest request);
}
