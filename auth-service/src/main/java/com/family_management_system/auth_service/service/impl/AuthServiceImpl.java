package com.family_management_system.auth_service.service.impl;

import com.family_management_system.auth_service.controller.UserClient;
import com.family_management_system.auth_service.dto.*;
import com.family_management_system.auth_service.security.JwtService;
import com.family_management_system.auth_service.service.AuthService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserClient userClient;
    private final JwtService jwtService;

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
        UserDto savedUser;
        try {
            savedUser = userClient.getUserByEmail(loginRequest.getEmail());
        } catch (FeignException ex) {
            if (isUserNotFound(ex)) {
                throw new BadCredentialsException("Invalid email or password");
            }
            throw ex;
        }
        if (savedUser==null || !passwordEncoder
                .matches(loginRequest.getPassword(),savedUser.getPassword())){
            throw new BadCredentialsException("Invalid email or password");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .roles("USER")
                .build();
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

    private boolean isUserNotFound(FeignException ex) {
        return ex.status() == 404 || ex.contentUTF8().contains("User not found");
    }
}
