package com.family_management_system.auth_service.controller;

import com.family_management_system.auth_service.dto.ApiResponse;
import com.family_management_system.auth_service.dto.AuthResponse;
import com.family_management_system.auth_service.dto.SignupRequest;
import com.family_management_system.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(
            @Valid @RequestBody SignupRequest signupRequest) {
        ApiResponse<AuthResponse> response = new ApiResponse<>(
                "Registration successful",
                LocalDateTime.now(),
                HttpStatus.CREATED,
                authService.signup(signupRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login() {
        ApiResponse<AuthResponse> response = new ApiResponse<>(
                "Login successful",
                LocalDateTime.now(),
                HttpStatus.OK,
                authService.login());
        return ResponseEntity.ok(response);
    }

    // @PostMapping("/logout")
    // public ResponseEntity<ApiResponse> logout(){
    // ApiResponse<> response = new ApiResponse();
    // return ResponseEntity.ok(response);
    // }
    //
    // @PostMapping("/change-password")
    // public ResponseEntity<ApiResponse> changePassword(){
    // ApiResponse<> response = new ApiResponse();
    // return ResponseEntity.ok(response);
    // }

}
