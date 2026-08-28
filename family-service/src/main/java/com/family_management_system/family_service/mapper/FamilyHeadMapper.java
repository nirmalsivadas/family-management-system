package com.family_management_system.family_service.mapper;

import com.family_management_system.family_service.dto.RegisterFamilyHeadRequest;
import com.family_management_system.family_service.dto.RegisterFamilyRequest;
import com.family_management_system.family_service.dto.RegisterResponse;
import com.family_management_system.family_service.entity.FamilyHead;
import com.family_management_system.family_service.enums.Status;

import java.util.Base64;
import java.util.Date;

public class FamilyHeadMapper {
    public static RegisterResponse toResponse(FamilyHead familyHead){
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setFamilyHeadName(familyHead.getFirstName()+" "+familyHead.getLastName());
        registerResponse.setRegistrationDate(familyHead.getJoinDate());
        registerResponse.setStatus(familyHead.getStatus());
        registerResponse.setNumberOfMembers(familyHead.getNumberOfFamilyMembers());
        registerResponse.setMemberShipNumber(familyHead.getMemberShipId());
        registerResponse.setFamilyName(familyHead.getFamilyName());
        if (familyHead.getPhoto()!=null && familyHead.getPhoto().length>0){
            String encodedPhoto = Base64.getEncoder().encodeToString(familyHead.getPhoto());
            registerResponse.setPhoto(encodedPhoto);
        }
        return registerResponse;
    }

    public static FamilyHead toEntity(RegisterFamilyRequest registerFamilyRequest
    ,byte[] photoBytes){
        if (registerFamilyRequest == null || registerFamilyRequest.getRegisterFamilyHeadRequest() == null) {
            throw new IllegalArgumentException("Registration request data or Family Head details cannot be null");
        }
        RegisterFamilyHeadRequest request = registerFamilyRequest.getRegisterFamilyHeadRequest();
        FamilyHead familyHead = new FamilyHead();
        familyHead.setFamilyName(request.getFamilyName());
        familyHead.setMemberShipType(request.getMemberShipType());
        familyHead.setFirstName(request.getFirstName());
        familyHead.setMiddleName(request.getMiddleName());
        familyHead.setLastName(request.getLastName());
        familyHead.setDateOfBirth(request.getDateOfBirth());
        familyHead.setGender(request.getGender());
        familyHead.setMaritalStatus(request.getMaritalStatus());
        familyHead.setBloodGroup(limit(defaultText(request.getBloodGroup(), "N/A"), 5));
        familyHead.setMobileNumber(request.getMobileNumber());
        familyHead.setAlternateMobile(request.getAlternateMobile());
        familyHead.setEmail(request.getEmail());
        familyHead.setOccupation(defaultText(request.getOccupation(), "N/A"));
        familyHead.setEmployment(request.getEmployment());
        familyHead.setDesignation(defaultText(request.getDesignation(), "N/A"));
        familyHead.setQualification(defaultText(request.getQualification(), "N/A"));
        familyHead.setProfession(defaultText(firstNonBlank(request.getProfession(), request.getEmployment(), request.getOccupation()), "N/A"));
        familyHead.setAnnualIncome(request.getAnnualIncome() != null ? request.getAnnualIncome() : 0L);
        familyHead.setAddressLine1(request.getAddressLine1());
        familyHead.setAddressLine2(request.getAddressLine2());
        familyHead.setCity(request.getCity());
        familyHead.setState(request.getState());
        familyHead.setPincode(request.getPinCode());
        familyHead.setPhoto(photoBytes);
        familyHead.setCountry(request.getCountry());
        familyHead.setJoinDate(new Date());
        familyHead.setStatus(Status.PENDING);
        familyHead.setOrganization(defaultText(request.getOrganization(), "N/A"));
        familyHead.setRegistrationCategory(request.getRegistrationCategory());
        return familyHead;
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
