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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Cacheable(value = "email",key = "email")
    public UserResponse findByEmail(String email) {
        User user =  userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return UserMapper.toResponse(user);
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
    public String updateProfile(UpdateProfileRequest updateProfileRequest) {
        User user = userRepository.findById(updateProfileRequest.getUserId())
                .orElseThrow(()->new RuntimeException("User not found"));

        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setMobileNumber(updateProfileRequest.getMobileNumber());
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
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        try {
            emailService.confirmPasswordChange(userEmail);
        } catch (Exception ignored) {
        }
        kafkaTemplate.send("password-changed",
                changePasswordRequest.getUserId().toString(),
                "A new password was created");
        return "Password Changed successfully";
    }

    @Override
    public String resetPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        String temporaryPassword = generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);
        kafkaTemplate.send("password-changed",
                user.getId().toString(),
                "A temporary password was issued");
        try {
            emailService.sendTemporaryPassword(email, temporaryPassword);
            return "Temporary password sent to your email";
        } catch (Exception e) {
            return "Email delivery failed. Temporary password: " + temporaryPassword;
        }
    }

    @Override
    public UserResponse createUser(SignupRequest signupRequest) {
        User user = UserMapper.toEntity(signupRequest);
        userRepository.save(user);
        return UserMapper.toResponse(user);
    }

    @Override
    @Cacheable(value = "userId",key = "#userId")
    public UserResponse findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new RuntimeException("User not found"));
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
