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
        familyMember.setLastName(registerFamilyMemberRequest.getLastName());
        familyMember.setDateOfBirth(registerFamilyMemberRequest.getDateOfBirth());
        familyMember.setGender(registerFamilyMemberRequest.getGender());
        familyMember.setMaritalStatus(registerFamilyMemberRequest.getMaritalStatus());
        familyMember.setBloodGroup(registerFamilyMemberRequest.getBloodGroup());
        familyMember.setMobileNumber(registerFamilyMemberRequest.getMobileNumber());
        familyMember.setEmail(registerFamilyMemberRequest.getEmail());
        familyMember.setOccupation(registerFamilyMemberRequest.getOccupation());
        familyMember.setEmployment(registerFamilyMemberRequest.getEmployment());
        familyMember.setOrganization(registerFamilyMemberRequest.getOrganization());
        familyMember.setProfession(registerFamilyMemberRequest.getProfession());
        familyMember.setQualification(registerFamilyMemberRequest.getQualification());
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
}
