package com.family_management_system.family_service.service;

import com.family_management_system.family_service.dto.UserResponse;
import com.family_management_system.family_service.entity.User;

public interface UserService {
    UserResponse findByEmail(String email);
}
