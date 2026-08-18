package com.family_management_system.master_service.service;

import com.family_management_system.master_service.dto.MasterResponse;

import java.util.List;

public interface MasterService {
    List<MasterResponse> getQualifications();
    List<MasterResponse> getProfessions();
    List<MasterResponse> getDesignations();
    List<MasterResponse> getBloodGroups();
}
