package com.family_management_system.family_service.controller;

import com.family_management_system.family_service.dto.ApiResponse;
import com.family_management_system.family_service.dto.ChangePasswordRequest;
import com.family_management_system.family_service.dto.UpdateProfileRequest;
import com.family_management_system.family_service.dto.UserResponse;
import com.family_management_system.family_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> findByEmail(String email){
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid
                                                              @RequestBody ChangePasswordRequest
                                                              changePasswordRequest){
        ApiResponse<String> response = new ApiResponse<>(
                "password changed successfully",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.changePassword(changePasswordRequest)
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

    @GetMapping("/{userId}/pending-status")
    public ResponseEntity<ApiResponse<Long>> pendingStatus(@PathVariable Long userId){
        ApiResponse<Long> response = new ApiResponse<>(
                "Fetched total members with pending status",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.pendingStatus(userId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/confirmed-status")
    public ResponseEntity<ApiResponse<Long>> confirmedStatus(@PathVariable Long userId){
        ApiResponse<Long> response = new ApiResponse<>(
                "Fetched total members with confirmed status",
                HttpStatus.OK,
                LocalDateTime.now(),
                userService.confirmedStatus(userId)
        );
        return ResponseEntity.ok(response);
    }

}
