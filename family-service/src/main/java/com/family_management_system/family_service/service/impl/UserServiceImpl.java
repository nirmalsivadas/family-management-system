package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.entity.FamilyHead;
import com.family_management_system.family_service.entity.User;
import com.family_management_system.family_service.enums.Status;
import com.family_management_system.family_service.mapper.UserMapper;
import com.family_management_system.family_service.repository.FamilyHeadRepository;
import com.family_management_system.family_service.repository.FamilyMemberRepository;
import com.family_management_system.family_service.repository.UserRepository;
import com.family_management_system.family_service.service.EmailService;
import com.family_management_system.family_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FamilyHeadRepository familyHeadRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public UserResponse findByEmail(String email) {
        User user =  userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return UserMapper.toResponse(user);
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
        kafkaTemplate.send("profile-updated",
                updateProfileRequest.getUserId().toString(),
                updateProfileRequest.getFirstName()+" updated the profile");
        return "Profile updated successfully";
    }

    @Override
    public String changePassword(String userEmail,ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getUserId())
                .orElseThrow(()->new RuntimeException("User not found"));

        if (!changePasswordRequest.getNewPassword().equals(
                changePasswordRequest.getConfirmNewPassword()
        )){
            throw new RuntimeException("Passwords do not match");
        }
        user.setPassword(changePasswordRequest.getNewPassword());
        emailService.confirmPasswordChange
                (userEmail);
        userRepository.save(user);
        kafkaTemplate.send("password-changed",
                changePasswordRequest.getUserId().toString(),
                "A new password was created");
        return "Password Changed successfully";
    }

    @Override
    public String changeStatus(Long userId, String status) {
        FamilyHead familyHead = familyHeadRepository.findByUserId(userId);
        familyHead.setStatus(Status.valueOf(status));
        familyHeadRepository.save(familyHead);
        kafkaTemplate.send("status-changed",userId.toString(),"Status changed");
        return "Status changed";
    }

    @Override
    public UserResponse createUser(SignupRequest signupRequest) {
        User user = UserMapper.toEntity(signupRequest);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new RuntimeException("User not found"));
        return UserMapper.toResponse(user);
    }

    @Override
    public String familiesWithStatus(Long userId, String status) {
        return String.valueOf(familyMemberRepository.countByStatus(Status.valueOf(status)));
    }
}
