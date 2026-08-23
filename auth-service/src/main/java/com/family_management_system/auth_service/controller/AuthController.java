package com.family_management_system.auth_service.controller;

import com.family_management_system.auth_service.dto.*;
import com.family_management_system.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
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
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResult loginResult = authService.login(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("jwt",loginResult.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();
        ApiResponse<AuthResponse> response = new ApiResponse<>(
                "Login successful",
                LocalDateTime.now(),
                HttpStatus.OK,
                loginResult.getAuthResponse()
        );
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString()).body(response);
    }

     @PostMapping("/logout")
     public ResponseEntity<ApiResponse<String>> logout(){
         ResponseCookie cookie = ResponseCookie.from("jwt","")
                 .httpOnly(true)
                 .secure(false)
                 .sameSite("Lax")
                 .path("/")
                 .maxAge(0)
                 .build();
         ApiResponse<String> response = new ApiResponse(
                 "logout successful",
                 LocalDateTime.now(),
                 HttpStatus.OK,
                 "logged out"
         );
         return ResponseEntity.ok().
                 header(HttpHeaders.SET_COOKIE,cookie.toString()).body(response);
     }
}
