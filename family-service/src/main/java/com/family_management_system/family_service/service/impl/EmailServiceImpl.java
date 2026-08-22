package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Override
    public String confirmPasswordChange() {
        return "";
    }
}
