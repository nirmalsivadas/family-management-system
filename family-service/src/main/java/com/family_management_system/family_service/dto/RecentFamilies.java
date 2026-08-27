package com.family_management_system.family_service.dto;

import com.family_management_system.family_service.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentFamilies {
    private String membershipId;
    private String familyHead;
    private Long numberOfFamilyMembers;
    private Status status;
}
