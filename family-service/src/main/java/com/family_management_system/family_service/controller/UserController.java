package com.family_management_system.family_service.controller;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody SignupRequest signupRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(signupRequest));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestParam String userEmail
            ,@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        ApiResponse<String> response = new ApiResponse<>(
                "password changed successfully",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.changePassword(userEmail,changePasswordRequest)
        );
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/update-profile")
    public ResponseEntity<ApiResponse<String>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest
    ){
        ApiResponse<String> response = new ApiResponse<>(
                "profile updated",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.updateProfile(updateProfileRequest)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ){
        ApiResponse<String> response = new ApiResponse<>(
                "temporary password sent",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.resetPassword(forgotPasswordRequest.getEmail())
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/total-families")
    public ResponseEntity<ApiResponse<Long>> totalFamilies(@PathVariable Long userId){
        ApiResponse<Long> response = new ApiResponse<>(
                "Total families registered",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.totalFamilies(userId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/total-members")
    public ResponseEntity<ApiResponse<Long>> totalMembers(@PathVariable Long userId){
        ApiResponse<Long> response = new ApiResponse<>(
                "Total members registered",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.totalMembers(userId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/{status}")
    public ResponseEntity<ApiResponse<Long>> familiesWithStatus(@PathVariable Long userId,
                                                            @PathVariable String status){
        ApiResponse<Long> response = new ApiResponse<>(
                "Number of families with" + status +" status fetched",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.familiesWithStatus(userId,status)
        );
        return ResponseEntity.ok(response);
    }

}
