package com.family_management_system.family_service.mapper;

import com.family_management_system.family_service.dto.UpdateProfileRequest;
import com.family_management_system.family_service.entity.User;

public class UserMapper {
    public static User toEntity(UpdateProfileRequest updateProfileRequest){
        User user = new User();
        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setMobileNumber(updateProfileRequest.getMobileNumber());
        return user;
    }
}
