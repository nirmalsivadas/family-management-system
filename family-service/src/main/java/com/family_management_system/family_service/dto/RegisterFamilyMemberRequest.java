package com.family_management_system.family_service.dto;

import com.family_management_system.family_service.enums.Relation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFamilyMemberRequest {
    private Relation relationShipWithFamilyHead;
    private String firstName;
    private String middleName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String bloodGroup;
    private Long mobileNumber;
    private String email;
    private String occupation;
    private String employment;
    private String qualification;
    private String profession;
    private String organization;
}
