package com.family_management_system.family_service.dto;

import com.family_management_system.family_service.enums.Status;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewFamilies {
    private String membershipId;
    private String familyHead;
    private String familyName;
    private Long numberOfFamilyMembers;
    private Date registrationDate;
    private Status status;
}
