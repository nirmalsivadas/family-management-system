package com.family_management_system.master_service.service;

import com.family_management_system.master_service.dto.MasterResponse;

import java.util.List;

public interface MasterService {
    List<MasterResponse> getQualifications();
    List<MasterResponse> getProfessions();
    List<MasterResponse> getDesignations();
    List<MasterResponse> getMaritalStatus();
    List<MasterResponse> getBloodGroups();
    List<MasterResponse> getOccupations();
    List<MasterResponse> getCities();
    List<MasterResponse> getCountries();
    List<MasterResponse> getStates();
    List<MasterResponse> getMemberShipTypes();
    List<MasterResponse> getGenders();
    List<MasterResponse> getRegistrationCategories();
}
