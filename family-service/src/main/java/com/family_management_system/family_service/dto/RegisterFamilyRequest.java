package com.family_management_system.family_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFamilyRequest {
    private Long id;
    private RegisterFamilyHeadRequest registerFamilyHeadRequest;
    private List<RegisterFamilyMemberRequest> registerFamilyMemberRequests;
}
