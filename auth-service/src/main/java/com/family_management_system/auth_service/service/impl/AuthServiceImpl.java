package com.family_management_system.auth_service.service.impl;

import com.family_management_system.auth_service.dto.AuthResponse;
import com.family_management_system.auth_service.dto.SignupRequest;
import com.family_management_system.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
        return null;
    }

    @Override
    public AuthResponse login() {
        return null;
    }
}
