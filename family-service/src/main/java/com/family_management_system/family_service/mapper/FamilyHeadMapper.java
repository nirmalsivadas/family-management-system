package com.family_management_system.family_service.mapper;

import com.family_management_system.family_service.dto.RegisterFamilyRequest;
import com.family_management_system.family_service.dto.RegisterResponse;
import com.family_management_system.family_service.entity.FamilyHead;

import java.util.Base64;

public class FamilyHeadMapper {
    public static RegisterResponse toResponse(FamilyHead familyHead){
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setFamilyHeadName(familyHead.getFamilyName());
        registerResponse.setFamilyHeadName(familyHead.getFirstName()+" "+familyHead.getLastName());
        registerResponse.setRegistrationDate(familyHead.getJoinDate());
        registerResponse.setStatus(familyHead.getStatus());
        registerResponse.setNumberOfMembers(familyHead.getNumberOfFamilyMembers());
        if (familyHead.getPhoto()!=null && familyHead.getPhoto().length>0){
            String encodedPhoto = Base64.getEncoder().encodeToString(familyHead.getPhoto());
            registerResponse.setPhoto(encodedPhoto);
        }
        return registerResponse;
    }

    public static FamilyHead toEntity(RegisterFamilyRequest registerFamilyRequest
    ,byte[] photoBytes){
        FamilyHead familyHead = new FamilyHead();
        familyHead.setFamilyName(registerFamilyRequest.getRegisterFamilyHeadRequest().getFamilyName());
        familyHead.setNumberOfFamilyMembers(registerFamilyRequest.getRegisterFamilyHeadRequest().getNumberOfFamilyMembers());
        familyHead.setMemberShipType(registerFamilyRequest.getRegisterFamilyHeadRequest().getMemberShipType());
        familyHead.setFirstName(registerFamilyRequest.getRegisterFamilyHeadRequest().getFirstName());
        familyHead.setLastName(registerFamilyRequest.getRegisterFamilyHeadRequest().getLastName());
        familyHead.setDateOfBirth(registerFamilyRequest.getRegisterFamilyHeadRequest().getDateOfBirth());
        familyHead.setGender(registerFamilyRequest.getRegisterFamilyHeadRequest().getGender());
        familyHead.setMaritalStatus(registerFamilyRequest.getRegisterFamilyHeadRequest().getMaritalStatus());
        familyHead.setBloodGroup(registerFamilyRequest.getRegisterFamilyHeadRequest().getBloodGroup());
        familyHead.setMobileNumber(registerFamilyRequest.getRegisterFamilyHeadRequest().getMobileNumber());
        familyHead.setEmail(registerFamilyRequest.getRegisterFamilyHeadRequest().getEmail());
        familyHead.setOccupation(registerFamilyRequest.getRegisterFamilyHeadRequest().getOccupation());
        familyHead.setDesignation(registerFamilyRequest.getRegisterFamilyHeadRequest().getDesignation());
        familyHead.setQualification(registerFamilyRequest.getRegisterFamilyHeadRequest().getQualification());
        familyHead.setProfession(registerFamilyRequest.getRegisterFamilyHeadRequest().getProfession());
        familyHead.setAnnualIncome(registerFamilyRequest.getRegisterFamilyHeadRequest().getAnnualIncome());
        familyHead.setAddressLine1(registerFamilyRequest.getRegisterFamilyHeadRequest().getAddressLine1());
        familyHead.setAddressLine2(registerFamilyRequest.getRegisterFamilyHeadRequest().getAddressLine2());
        familyHead.setCity(registerFamilyRequest.getRegisterFamilyHeadRequest().getCity());
        familyHead.setState(registerFamilyRequest.getRegisterFamilyHeadRequest().getState());
        familyHead.setPincode(registerFamilyRequest.getRegisterFamilyHeadRequest().getPinCode());
        familyHead.setPhoto(photoBytes);
        return familyHead;
    }
//
//    public static FamilyHead updateFamilyHeadEntity(UpdateFamilyRequest updateFamilyRequest){
//        FamilyHead familyHead = new FamilyHead();
//
//        private String familyName;
//        private Long numberOfFamilyMembers;
//        private String memberShipType;
//        private String registrationCategory;
//        private String firstName;
//        private String lastName;
//        private String gender;
//        private String maritalStatus;
//        private String bloodGroup;
//        private Long mobileNumber;
//        private String occupation;
//        private String profession;
//        private String qualification;
//        private String designation;
//        private Long annualIncome;
//        private String addressLine1;
//        private String addressLine2;
//        private String city;
//        private String country;
//        private String state;
//        private String pinCode;
//        private byte[] photo;
//        return familyHead;
//    }
}
