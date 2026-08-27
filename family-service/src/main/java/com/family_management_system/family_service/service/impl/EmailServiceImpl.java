package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.host}")
    private String senderHost;

    @Value("${spring.mail.port}")
    private int senderPort;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${spring.mail.password}")
    private String senderPassword;

    @Override
    public void confirmPasswordChange(String userEmail) {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(senderHost);
        javaMailSenderImpl.setPort(senderPort);
        javaMailSenderImpl.setUsername(senderEmail);
        javaMailSenderImpl.setPassword(senderPassword);

        Properties props = javaMailSenderImpl.getJavaMailProperties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(userEmail);
        message.setSubject("Password Change Success");
        message.setText("Dear user, you have successfully changed your password.\n" +
                "From now on please use the new password for login.");
        javaMailSenderImpl.send(message);
    }
}
