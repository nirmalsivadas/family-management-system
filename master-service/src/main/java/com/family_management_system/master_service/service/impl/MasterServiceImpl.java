package com.family_management_system.master_service.service.impl;

import com.family_management_system.master_service.dto.MasterResponse;
import com.family_management_system.master_service.repository.*;
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
    private final OccupationRepository occupationRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final RegistrationCategoriesRepository registrationCategoriesRepository;
    private final MemberShipTypesRepository memberShipTypesRepository;
    private final GenderRepository genderRepository;
    private final MaritalStatusRepository maritalStatusRepository;

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

    @Override
    public List<MasterResponse> getOccupations() {
        return occupationRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getCities() {
        return cityRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getCountries() {
        return countryRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getStates() {
        return stateRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getMemberShipTypes() {
        return memberShipTypesRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getGenders() {
        return genderRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getMaritalStatus() {
        return maritalStatusRepository.findAllNames();
    }

    @Override
    public List<MasterResponse> getRegistrationCategories() {
        return registrationCategoriesRepository.findAllNames();
    }
}
