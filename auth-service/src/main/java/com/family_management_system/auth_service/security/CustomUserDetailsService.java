package com.family_management_system.auth_service.security;

import com.family_management_system.auth_service.controller.UserClient;
import com.family_management_system.auth_service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto savedUser = userClient.getUserByEmail(email);
        if (savedUser==null){
            throw new RuntimeException("User not found with this email:"+email);
        }
        return User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .roles("USER")
                .build();
    }
}
