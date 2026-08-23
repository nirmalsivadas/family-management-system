package com.family_management_system.auth_service.service;

import com.family_management_system.auth_service.dto.AuthResponse;
import com.family_management_system.auth_service.dto.LoginRequest;
import com.family_management_system.auth_service.dto.LoginResult;
import com.family_management_system.auth_service.dto.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest signupRequest);
    LoginResult login(LoginRequest loginRequest);
}
