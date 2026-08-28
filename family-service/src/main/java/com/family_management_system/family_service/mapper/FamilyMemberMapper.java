package com.family_management_system.family_service.mapper;

import com.family_management_system.family_service.dto.RegisterFamilyMemberRequest;
import com.family_management_system.family_service.dto.RegisterFamilyRequest;
import com.family_management_system.family_service.dto.UpdateFamilyMemberRequest;
import com.family_management_system.family_service.dto.UpdateFamilyRequest;
import com.family_management_system.family_service.entity.FamilyMember;
import com.family_management_system.family_service.enums.Relation;
import com.family_management_system.family_service.enums.Status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FamilyMemberMapper {
    public static FamilyMember toEntity(RegisterFamilyMemberRequest registerFamilyMemberRequest){
        if (registerFamilyMemberRequest==null){
            return null;
        }
        FamilyMember familyMember = new FamilyMember();
        familyMember.setFirstName(registerFamilyMemberRequest.getFirstName());
        familyMember.setMiddleName(registerFamilyMemberRequest.getMiddleName());
        familyMember.setLastName(registerFamilyMemberRequest.getLastName());
        familyMember.setDateOfBirth(registerFamilyMemberRequest.getDateOfBirth());
        familyMember.setGender(registerFamilyMemberRequest.getGender());
        familyMember.setMaritalStatus(defaultText(registerFamilyMemberRequest.getMaritalStatus(), "N/A"));
        familyMember.setBloodGroup(limit(defaultText(registerFamilyMemberRequest.getBloodGroup(), "N/A"), 5));
        familyMember.setMobileNumber(registerFamilyMemberRequest.getMobileNumber() != null ? registerFamilyMemberRequest.getMobileNumber() : 0L);
        familyMember.setEmail(defaultText(registerFamilyMemberRequest.getEmail(), "N/A"));
        familyMember.setOccupation(defaultText(registerFamilyMemberRequest.getOccupation(), "N/A"));
        familyMember.setEmployment(defaultText(registerFamilyMemberRequest.getEmployment(), "N/A"));
        familyMember.setOrganization(defaultText(registerFamilyMemberRequest.getOrganization(), "N/A"));
        familyMember.setProfession(defaultText(firstNonBlank(registerFamilyMemberRequest.getProfession(), registerFamilyMemberRequest.getOccupation()), "N/A"));
        familyMember.setQualification(defaultText(registerFamilyMemberRequest.getQualification(), "N/A"));
        familyMember.setStatus(Status.PENDING);
        familyMember.setRelationShipWithFamilyHead(registerFamilyMemberRequest.getRelationShipWithFamilyHead());
        return familyMember;
    }
        public static List<FamilyMember> toListEntity(List<RegisterFamilyMemberRequest> registerFamilyMemberRequestList){
        if (registerFamilyMemberRequestList==null || registerFamilyMemberRequestList.isEmpty()){
            return Collections.emptyList();
        }
        return registerFamilyMemberRequestList
                .stream()
                .map(FamilyMemberMapper::toEntity)
                .collect(Collectors.toList());
    }

    private static String defaultText(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }

    private static String limit(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}
