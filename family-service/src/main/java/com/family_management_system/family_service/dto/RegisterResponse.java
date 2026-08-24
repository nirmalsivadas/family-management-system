package com.family_management_system.family_service.dto;

import com.family_management_system.family_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String memberShipNumber;
    private String familyName;
    private Date registrationDate;
    private String familyHeadName;
    private Long numberOfMembers;
    private Status status;
    private String photo;
}
