package com.family_management_system.family_service.service;

import com.family_management_system.family_service.dto.ChangePasswordRequest;
import com.family_management_system.family_service.dto.SignupRequest;
import com.family_management_system.family_service.dto.UpdateProfileRequest;
import com.family_management_system.family_service.dto.UserResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public interface UserService {
    UserResponse findByEmail(String email);
    Long totalFamilies(Long userId);
    Long totalMembers(Long userId);
    String updateProfile(UpdateProfileRequest updateProfileRequest);
    String changePassword(String userEmail,ChangePasswordRequest changePasswordRequest);
    Long pendingStatus(Long userId);
    Long confirmedStatus(Long userId);
    String changeStatus(Long userId, String status);

    UserResponse createUser(SignupRequest signupRequest);

    UserResponse findByUserId(Long userId);
}
