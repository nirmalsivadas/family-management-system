package com.family_management_system.family_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFamilyRequest {
    private String memberShipId;
    private UpdateFamilyHeadRequest updateFamilyHeadRequest;
    private UpdateFamilyMemberRequest updateFamilyMemberRequest;
}
