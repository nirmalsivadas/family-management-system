package com.family_management_system.family_service.service;

import com.family_management_system.family_service.dto.ChangePasswordRequest;
import com.family_management_system.family_service.dto.UpdateProfileRequest;
import com.family_management_system.family_service.dto.UserResponse;
import jakarta.validation.Valid;

public interface UserService {
    UserResponse findByEmail(String email);
    Long totalFamilies(Long userId);
    Long totalMembers(Long userId);
    String updateProfile(@Valid UpdateProfileRequest updateProfileRequest);
    String changePassword(@Valid ChangePasswordRequest changePasswordRequest);
    Long pendingStatus(Long userId);
    Long confirmedStatus(Long userId);
}
