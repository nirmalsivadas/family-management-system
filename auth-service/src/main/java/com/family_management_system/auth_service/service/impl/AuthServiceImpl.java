package com.family_management_system.auth_service.service.impl;

import com.family_management_system.auth_service.controller.UserClient;
import com.family_management_system.auth_service.dto.*;
import com.family_management_system.auth_service.security.CustomUserDetailsService;
import com.family_management_system.auth_service.security.JwtService;
import com.family_management_system.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserClient userClient;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public AuthResponse signup(SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        UserDto savedUser = userClient.createUser(signupRequest);
        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getMobileNumber()
        );
    }

    @Override
    public LoginResult login(LoginRequest loginRequest) {
        UserDto savedUser = userClient.getUserByEmail(loginRequest.getEmail());
        if (savedUser==null || !passwordEncoder
                .matches(loginRequest.getPassword(),savedUser.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtService.generateToken(userDetails);
        AuthResponse response = new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getMobileNumber()
        );
        return new LoginResult(response,token);
    }
}
