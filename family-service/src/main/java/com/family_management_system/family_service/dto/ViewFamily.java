package com.family_management_system.family_service.dto;

import com.family_management_system.family_service.entity.FamilyMember;
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
    private String familyName;
    private Date registrationDate;
    private Long numberOfFamilyMembers;
    private String familyMemberShipType;
    private String status;
    private List<FamilyMember> familyMembers;
    private String address;
    private byte[] photo;
    private String familHeadName;
    private String familyHeadGender;
    private Long mobileNumber;
    private String email;
    private String bloodGroup;
    private String organization;
    private Long annualIncome;
    private String designation;
}
