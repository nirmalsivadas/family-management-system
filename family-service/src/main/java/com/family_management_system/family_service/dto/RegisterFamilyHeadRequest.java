package com.family_management_system.family_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFamilyRequest {
    private String familyName;
    private int numberOfFamilyMembers;
    private String memberShipType;
    private String registrationCategory;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String maritalStatus;
    private String bloodGroup;
    private Long mobileNumber;
    private String email;
    private String occupation;
    private String employment;
    private String employer;
    private String designation;
    private Long annualIncome;
}
