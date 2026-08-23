package com.family_management_system.family_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String password;

}
