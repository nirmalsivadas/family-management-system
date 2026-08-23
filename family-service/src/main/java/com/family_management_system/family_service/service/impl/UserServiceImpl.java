package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.entity.User;
import com.family_management_system.family_service.enums.Status;
import com.family_management_system.family_service.mapper.UserMapper;
import com.family_management_system.family_service.repository.FamilyHeadRepository;
import com.family_management_system.family_service.repository.FamilyMemberRepository;
import com.family_management_system.family_service.repository.UserRepository;
import com.family_management_system.family_service.service.EmailService;
import com.family_management_system.family_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FamilyHeadRepository familyHeadRepository;
    private final FamilyMemberRepository familyMemberRepository;

    @Override
    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Long totalFamilies(Long userId) {
        return familyHeadRepository.countByUserId(userId);
    }

    @Override
    public Long totalMembers(Long userId) {
        Long total_members =
                familyHeadRepository.countByUserId(userId)+
                        familyMemberRepository.countByFamilyHeadUserId(userId);
        return total_members;
    }

    @Override
    public String updateProfile(UpdateProfileRequest updateProfileRequest) {
        User user1 = userRepository.findById(updateProfileRequest.getUserId())
                .orElseThrow(()->new RuntimeException("User not found"));

        User user =  UserMapper.toUpdateEntity(updateProfileRequest);
        userRepository.save(user);
        return "Profile updated successfully";
    }

    @Override
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getUserId())
                .orElseThrow(()->new RuntimeException("User not found"));

        if (!changePasswordRequest.getNewPassword().equals(
                changePasswordRequest.getConfirmNewPassword()
        )){
            throw new RuntimeException("Passwords do not match");
        }

        emailService.confirmPasswordChange();
        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
        return "Password Changed successfully";
    }

    @Override
    public Long pendingStatus(Long userId) {
        return familyMemberRepository.countByStatus(Status.PENDING);
    }

    @Override
    public Long confirmedStatus(Long userId) {
        return familyMemberRepository.countByStatus(Status.CONFIRMED);
    }

    @Override
    public UserResponse createUser(SignupRequest signupRequest) {
        User user = UserMapper.toEntity(signupRequest);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }
}
