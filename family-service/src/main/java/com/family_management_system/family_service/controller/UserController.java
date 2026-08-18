package com.family_management_system.family_service.controller;

import com.family_management_system.family_service.dto.UserResponse;
import com.family_management_system.family_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> findByEmail(String email){
        return ResponseEntity.ok(userService.findByEmail(email));
    }
}
