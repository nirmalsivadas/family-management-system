package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.entity.FamilyHead;
import com.family_management_system.family_service.entity.FamilyMember;
import com.family_management_system.family_service.entity.User;
import com.family_management_system.family_service.enums.Status;
import com.family_management_system.family_service.mapper.FamilyHeadMapper;
import com.family_management_system.family_service.mapper.FamilyMemberMapper;
import com.family_management_system.family_service.repository.FamilyHeadRepository;
import com.family_management_system.family_service.repository.FamilyMemberRepository;
import com.family_management_system.family_service.repository.UserRepository;
import com.family_management_system.family_service.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyServiceImpl implements FamilyService {

    private final UserRepository userRepository;
    private final FamilyHeadRepository familyHeadRepository;
    private final FamilyMemberRepository familyMemberRepository;

    @Override
    public RegisterResponse registerFamily(RegisterFamilyRequest registerFamilyRequest) {
        User user = userRepository.findById(registerFamilyRequest.getUserId())
                .orElseThrow(()->new RuntimeException("User not found"));

        FamilyHead familyHead = FamilyHeadMapper.toEntity(registerFamilyRequest);
        FamilyMember familyMember = FamilyMemberMapper.toEntity(registerFamilyRequest);
        familyHead.setNumberOfFamilyMembers(familyHead.getNumberOfFamilyMembers()+1);
        familyHeadRepository.save(familyHead);
        familyMemberRepository.save(familyMember);
        return FamilyHeadMapper.toResponse(familyHead);
    }

    @Override
    public Page<ViewFamilies> viewFamilies(Long userId,String status, int page, int size) {
        Pageable pageable = PageRequest.of(page,size,
                Sort.by("joinDate").descending());

        Page<FamilyHead> familyHeads;

        if (status==null || status.trim().equalsIgnoreCase("ALL")){
            familyHeads = familyHeadRepository.findByUserId(userId,pageable);
        }
        else {
            Status statusEnum = Status.valueOf(status.trim().toUpperCase());
            familyHeads =
                    familyHeadRepository.findByUserIdAndStatus(userId,statusEnum,pageable);
        }

        return familyHeads.map(fh->
                new ViewFamilies(
                     fh.getMemberShipId(),
                        fh.getFirstName()+" "+fh.getLastName(),
                        fh.getFamilyName(),
                        fh.getNumberOfFamilyMembers(),
                        fh.getJoinDate(),
                        fh.getStatus() != null ? fh.getStatus() : null
                ));
    }

    @Override
    public Page<ViewMembers> viewMembers(Long userId,int page,int size) {
        Pageable pageable = PageRequest.of(page,size,
                Sort.by("joinDate").descending());

        Page<FamilyMember> familyMembers = familyMemberRepository
                .findByFamilyHeadUserId(userId,pageable);

        return familyMembers.map(fh->
                new ViewMembers(
                        fh.getFirstName()+" "+fh.getLastName(),
                        fh.getRelation().getName(),
                        fh.getFamilyHead().getFamilyName(),
                        fh.getFamilyHead().getMemberShipId(),
                        fh.getOccupation(),
                        fh.getMobileNumber(),
                        fh.getStatus()
                ));
    }

    @Override
    public ViewFamily viewFamily(Long userId,String memberShipId) {
        FamilyHead familyHead = familyHeadRepository.findByUserIdAndMemberShipId(userId,memberShipId);

        ViewFamily viewFamily = new ViewFamily();
        viewFamily.setFamilyName(familyHead.getFamilyName());
        viewFamily.setRegistrationDate(familyHead.getJoinDate());
        viewFamily.setNumberOfFamilyMembers(familyHead.getNumberOfFamilyMembers());
        viewFamily.setFamilyMemberShipType(familyHead.getMemberShipType());
        viewFamily.setStatus(familyHead.getStatus().name());
        viewFamily.setAddress(familyHead.getAddressLine1()+" "+familyHead.getAddressLine2());
        viewFamily.setMobileNumber(familyHead.getMobileNumber());
        viewFamily.setFamilyHeadGender(familyHead.getGender());
        viewFamily.setFamilHeadName(familyHead.getFirstName()+" "+familyHead.getLastName());
        viewFamily.setBloodGroup(familyHead.getBloodGroup());
        viewFamily.setEmail(familyHead.getEmail());
        viewFamily.setDesignation(familyHead.getDesignation());
        viewFamily.setAnnualIncome(familyHead.getAnnualIncome());
        viewFamily.setOrganization(familyHead.getOrganization());
//        private List<FamilyMember> familyMembers;
//        private byte[] photo;
        return viewFamily;
    }

    @Override
    public String updateFamily(UpdateFamilyRequest updateFamilyRequest) {
//        FamilyHead familyHead = FamilyHeadMapper.updateFamilyHeadEntity(updateFamilyRequest);
//        FamilyMember familyMember = FamilyMemberMapper.updateFamilyMemberEntity(updateFamilyRequest);
//        familyHeadRepository.save(familyHead);
//        familyMemberRepository.save(familyMember);
        return "Family updated";
    }
}
