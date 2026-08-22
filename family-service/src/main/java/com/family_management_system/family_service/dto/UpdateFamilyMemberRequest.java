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
public class UpdateFamilyMemberRequest {
    private String relationShipWithFamilyHead;
    private String firstName;
    private String lastName;
    private String gender;
    private String maritalStatus;
    private String bloodGroup;
    private Long mobileNumber;
    private String occupation;
    private String employment;
}
