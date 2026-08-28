package com.family_management_system.family_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewFamily {
    private String membershipId;
    private String familyName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date registrationDate;
    private Long numberOfFamilyMembers;
    private String familyMemberShipType;
    private String registrationCategory;
    private String status;
    private List<FamilyMemberView> familyMembers;
    private String address;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private String photo;
    private String firstName;
    private String middleName;
    private String lastName;
    private String familHeadName;
    private String familyHeadGender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String maritalStatus;
    private Long mobileNumber;
    private Long alternateMobile;
    private String email;
    private String bloodGroup;
    private String occupation;
    private String employment;
    private String organization;
    private Long annualIncome;
    private String designation;
    private String qualification;
    private String profession;
}
