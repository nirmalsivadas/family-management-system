package com.family_management_system.master_service.service.impl;

import com.family_management_system.master_service.dto.MasterResponse;
import com.family_management_system.master_service.repository.BloodGroupRepository;
import com.family_management_system.master_service.repository.DesignationRepository;
import com.family_management_system.master_service.repository.ProfessionRepository;
import com.family_management_system.master_service.repository.QualificationRepository;
import com.family_management_system.master_service.service.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {
    private final QualificationRepository qualificationRepository;
    private final ProfessionRepository professionRepository;
    private final DesignationRepository designationRepository;
    private final BloodGroupRepository bloodGroupRepository;

    @Override
    public List<MasterResponse> getQualifications() {
        return qualificationRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getProfessions() {
        return professionRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getDesignations() {
        return designationRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getBloodGroups() {
        return bloodGroupRepository.findAllNames();
    }
}
