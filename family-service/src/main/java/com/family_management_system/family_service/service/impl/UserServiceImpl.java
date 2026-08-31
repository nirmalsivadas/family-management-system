package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.entity.User;
import com.family_management_system.family_service.enums.Status;
import com.family_management_system.family_service.exception.ResourceNotFoundException;
import com.family_management_system.family_service.mapper.UserMapper;
import com.family_management_system.family_service.repository.FamilyHeadRepository;
import com.family_management_system.family_service.repository.FamilyMemberRepository;
import com.family_management_system.family_service.repository.UserRepository;
import com.family_management_system.family_service.service.EmailService;
import com.family_management_system.family_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FamilyHeadRepository familyHeadRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        User user =  userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        return UserMapper.toAuthResponse(user);
    }

    @Override
    @Cacheable(value = "total-families",key = "#userId")
    public Long totalFamilies(Long userId) {
        return familyHeadRepository.countByUserId(userId);
    }

    @Override
    @Cacheable(value = "total-members",key = "#userId")
    public Long totalMembers(Long userId) {
        Long total_members =
                familyHeadRepository.countByUserId(userId)+
                        familyMemberRepository.countByFamilyHeadUserId(userId);
        return total_members;
    }

    @Override
    @CacheEvict(value = "userId", key = "#updateProfileRequest.userId")
    public String updateProfile(UpdateProfileRequest updateProfileRequest) {
        return updateProfile(updateProfileRequest, null);
    }

    @Override
    @CacheEvict(value = "userId", key = "#updateProfileRequest.userId")
    public String updateProfile(UpdateProfileRequest updateProfileRequest, MultipartFile photo) {
        User user = userRepository.findById(updateProfileRequest.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setMobileNumber(updateProfileRequest.getMobileNumber());
        if (photo != null && !photo.isEmpty()) {
            try {
                user.setPhoto(photo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile picture file data", e);
            }
        }
        userRepository.save(user);
        kafkaTemplate.send("profile-updated",
                updateProfileRequest.getUserId().toString(),
                updateProfileRequest.getFirstName()+" updated the profile");
        return "Profile updated successfully";
    }

    @Override
    @Transactional
    public String changePassword(String userEmail,ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        if (!changePasswordRequest.getNewPassword().equals(
                changePasswordRequest.getConfirmNewPassword()
        )){
            throw new RuntimeException("Passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        emailService.confirmPasswordChange(user.getEmail());
        kafkaTemplate.send("password-changed",
                changePasswordRequest.getUserId().toString(),
                "A new password was created");
        return "Password changed successfully. Confirmation email sent to " + user.getEmail();
    }

    @Override
    @Transactional
    public String resetPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        String temporaryPassword = generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);
        emailService.sendTemporaryPassword(user.getEmail(), temporaryPassword);
        kafkaTemplate.send("password-changed",
                user.getId().toString(),
                "A temporary password was issued");
        return "Temporary password sent to " + user.getEmail();
    }

    @Override
    public UserResponse createUser(SignupRequest signupRequest) {
        User user = UserMapper.toEntity(signupRequest);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    @Override
    @Cacheable(value = "userId",key = "#userId")
    @Transactional(readOnly = true)
    public UserResponse findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not found"));
        return UserMapper.toResponse(user);
    }

    @Override
    @Cacheable(value = "families-with-status",key = "#userId + ':' + #status")
    public Long familiesWithStatus(Long userId, String status) {
        return familyHeadRepository.countByUserIdAndStatus(userId, Status.valueOf(status));
    }

    private String generateTemporaryPassword() {
        String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }
        return builder.toString();
    }
}
