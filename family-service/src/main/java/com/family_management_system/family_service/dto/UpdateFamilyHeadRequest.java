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
public class UpdateFamilyHeadRequest {
    private String familyName;
    private Long numberOfFamilyMembers;
    private String memberShipType;
    private String registrationCategory;
    private String firstName;
    private String lastName;
    private String gender;
    private String maritalStatus;
    private String bloodGroup;
    private Long mobileNumber;
    private String occupation;
    private String profession;
    private String qualification;
    private String designation;
    private Long annualIncome;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
    private String state;
    private String pinCode;
    private byte[] photo;
}
