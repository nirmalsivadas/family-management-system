package com.family_management_system.family_service.mapper;

import com.family_management_system.family_service.dto.RegisterFamilyRequest;
import com.family_management_system.family_service.dto.UpdateFamilyRequest;
import com.family_management_system.family_service.entity.FamilyMember;

public class FamilyMemberMapper {
    public static FamilyMember toEntity(RegisterFamilyRequest registerFamilyRequest){
        FamilyMember familyMember = new FamilyMember();
        familyMember.setFirstName(registerFamilyRequest.getRegisterFamilyMemberRequest().getFirstName());
        familyMember.setLastName(registerFamilyRequest.getRegisterFamilyMemberRequest().getLastName());
        familyMember.setDateOfBirth(registerFamilyRequest.getRegisterFamilyMemberRequest().getDateOfBirth());
        familyMember.setGender(registerFamilyRequest.getRegisterFamilyMemberRequest().getGender());
        familyMember.setMaritalStatus(registerFamilyRequest.getRegisterFamilyMemberRequest().getMaritalStatus());
        familyMember.setBloodGroup(registerFamilyRequest.getRegisterFamilyMemberRequest().getBloodGroup());
        familyMember.setMobileNumber(registerFamilyRequest.getRegisterFamilyMemberRequest().getMobileNumber());
        familyMember.setEmail(registerFamilyRequest.getRegisterFamilyMemberRequest().getEmail());
        familyMember.setOccupation(registerFamilyRequest.getRegisterFamilyMemberRequest().getOccupation());
        familyMember.setEmployment(registerFamilyRequest.getRegisterFamilyMemberRequest().getEmployment());
        return familyMember;
    }

    public static FamilyMember updateFamilyMemberEntity(UpdateFamilyRequest updateFamilyRequest){
        FamilyMember familyMember = new FamilyMember();
//        familyMember.setRelation(updateFamilyRequest.getUpdateFamilyMemberRequest().getRelationShipWithFamilyHead());
        familyMember.setFirstName(updateFamilyRequest.getUpdateFamilyMemberRequest().getFirstName());
        familyMember.setLastName(updateFamilyRequest.getUpdateFamilyMemberRequest().getLastName());
        familyMember.setGender(updateFamilyRequest.getUpdateFamilyMemberRequest().getGender());
        familyMember.setMaritalStatus(updateFamilyRequest.getUpdateFamilyMemberRequest().getMaritalStatus());
        familyMember.setBloodGroup(updateFamilyRequest.getUpdateFamilyMemberRequest().getBloodGroup());
        familyMember.setMobileNumber(updateFamilyRequest.getUpdateFamilyMemberRequest().getMobileNumber());
        familyMember.setOccupation(updateFamilyRequest.getUpdateFamilyMemberRequest().getOccupation());
        familyMember.setEmployment(updateFamilyRequest.getUpdateFamilyMemberRequest().getEmployment());
        return familyMember;
    }
}
