package com.family_management_system.auth_service.service;

import com.family_management_system.auth_service.dto.AuthResponse;
import com.family_management_system.auth_service.dto.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest signupRequest);
    AuthResponse login();
}
