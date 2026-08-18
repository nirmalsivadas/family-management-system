package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.dto.UserResponse;
import com.family_management_system.family_service.entity.User;
import com.family_management_system.family_service.repository.UserRepository;
import com.family_management_system.family_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
