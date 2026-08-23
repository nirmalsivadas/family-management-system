package com.family_management_system.family_service.mapper;

import com.family_management_system.family_service.dto.SignupRequest;
import com.family_management_system.family_service.dto.UpdateProfileRequest;
import com.family_management_system.family_service.dto.UserResponse;
import com.family_management_system.family_service.entity.User;

public class UserMapper {
    public static User toUpdateEntity(UpdateProfileRequest updateProfileRequest){
        User user = new User();
        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setMobileNumber(updateProfileRequest.getMobileNumber());
        return user;
    }

    public static User toEntity(SignupRequest signupRequest){
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setMobileNumber(signupRequest.getMobileNumber());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        return user;
    }

    public static UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setMobileNumber(user.getMobileNumber());
        return userResponse;
    }
}
