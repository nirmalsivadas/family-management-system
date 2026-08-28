package com.family_management_system.family_service.service;

public interface EmailService {
    void confirmPasswordChange(String userEmail);
    void sendTemporaryPassword(String userEmail, String temporaryPassword);
}
