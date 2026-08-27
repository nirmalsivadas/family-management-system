package com.family_management_system.family_service.service;

import lombok.Value;

public interface EmailService {
    void confirmPasswordChange(String userEmail);
}
