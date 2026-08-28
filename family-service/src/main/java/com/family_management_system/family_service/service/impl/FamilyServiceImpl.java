package com.family_management_system.family_service.service.impl;

import com.family_management_system.family_service.dto.*;
import com.family_management_system.family_service.entity.FamilyHead;
import com.family_management_system.family_service.entity.FamilyMember;
import com.family_management_system.family_service.entity.User;
import com.family_management_system.family_service.enums.Relation;
import com.family_management_system.family_service.enums.Status;
import com.family_management_system.family_service.mapper.FamilyHeadMapper;
import com.family_management_system.family_service.mapper.FamilyMemberMapper;
import com.family_management_system.family_service.repository.FamilyHeadRepository;
import com.family_management_system.family_service.repository.FamilyMemberRepository;
import com.family_management_system.family_service.repository.RelationRepository;
import com.family_management_system.family_service.repository.UserRepository;
import com.family_management_system.family_service.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyServiceImpl implements FamilyService {

    private final UserRepository userRepository;
    private final FamilyHeadRepository familyHeadRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final RelationRepository relationRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public RegisterResponse registerFamily(
            RegisterFamilyRequest registerFamilyRequest,
            MultipartFile photo){
        byte[] photoBytes = null;
        try{
            photoBytes = (photo!=null) ? photo.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read profile picture file data", e);
        }
        User user = userRepository.findById(registerFamilyRequest.getUserId())
                .orElseThrow(()->new RuntimeException("User not found"));
        FamilyHead familyHead = FamilyHeadMapper.toEntity(registerFamilyRequest,photoBytes);
        List<RegisterFamilyMemberRequest> registerFamilyMemberRequestList = registerFamilyRequest.getRegisterFamilyMemberRequests();
        List<FamilyMember> familyMembers = FamilyMemberMapper.toListEntity(registerFamilyMemberRequestList);
        long memberCount = (registerFamilyMemberRequestList!= null ? registerFamilyMemberRequestList.size() : 0) + 1;
        familyHead.setNumberOfFamilyMembers(memberCount);
        familyHead.setUser(user);
        familyHeadRepository.save(familyHead);
        if (familyMembers!=null && !familyMembers.isEmpty()){
            for (FamilyMember familyMember : familyMembers){
                familyMember.setFamilyHead(familyHead);
            }
            familyMemberRepository.saveAll(familyMembers);
        }
        kafkaTemplate.send("family-registered",
                registerFamilyRequest.getUserId().toString(),
                familyHead.getFamilyName()+" was registered");
        kafkaTemplate.send("family-members-added",
                registerFamilyRequest.getUserId().toString(),
                familyHead.getNumberOfFamilyMembers()+" family members were added");
        return FamilyHeadMapper.toResponse(familyHead);
    }

    @Override
    @Cacheable(value = "view-families",key = "#userId + ':' + #status + ':' + #page + ':' + #size")
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
    @Transactional(readOnly = true)
    @Cacheable(value = "view-members",key = "#userId + ':' + #page + ':' + #size")
    public Page<ViewMembers> viewMembers(Long userId,int page,int size) {
        Pageable pageable = PageRequest.of(page,size);

        Page<FamilyMember> familyMembers = familyMemberRepository
                .findByFamilyHeadUserIdOrderByFamilyHeadJoinDateDesc(userId,pageable);

        return familyMembers.map(fh->
                new ViewMembers(
                        fh.getFirstName()+" "+fh.getLastName(),
                        fh.getRelationShipWithFamilyHead().name(),
                        fh.getFamilyHead().getFamilyName(),
                        fh.getFamilyHead().getMemberShipId(),
                        fh.getOccupation(),
                        fh.getMobileNumber(),
                        fh.getStatus()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "view-family",key = "#userId + '_' + #memberShipId")
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
        if (familyHead.getPhoto()!=null && familyHead.getPhoto().length>0){
            String encodedPhoto = Base64.getEncoder().encodeToString(familyHead.getPhoto());
            viewFamily.setPhoto(encodedPhoto);
        }
        List<FamilyMember> familyMembers = familyMemberRepository
                .findByFamilyHeadMemberShipId(memberShipId);
        viewFamily.setFamilyMembers(familyMembers);
        return viewFamily;
    }

    @Override
    @Transactional
    @CacheEvict(value = {
            "view-families",
            "view-members",
            "view-family",
            "recent-families"
    }, key = "#userId", allEntries = true)
    public String updateFamily(Long userId,UpdateFamilyRequest updateFamilyRequest) {
        if (updateFamilyRequest == null || updateFamilyRequest.getMemberShipId() == null) {
            throw new RuntimeException("Membership id is required");
        }

        FamilyHead familyHead = familyHeadRepository.findByUserIdAndMemberShipId(
                userId,
                updateFamilyRequest.getMemberShipId()
        );

        if (familyHead == null) {
            throw new RuntimeException("Family not found");
        }

        updateFamilyHead(familyHead, updateFamilyRequest.getUpdateFamilyHeadRequest());
        familyHeadRepository.save(familyHead);

        UpdateFamilyMemberRequest memberRequest = updateFamilyRequest.getUpdateFamilyMemberRequest();
        if (memberRequest != null && memberRequest.getFamilyMemberId() != null) {
            FamilyMember familyMember = familyMemberRepository
                    .findByIdAndFamilyHeadUserIdAndFamilyHeadMemberShipId(
                            memberRequest.getFamilyMemberId(),
                            userId,
                            updateFamilyRequest.getMemberShipId()
                    )
                    .orElseThrow(() -> new RuntimeException("Family member not found"));

            updateFamilyMember(familyMember, memberRequest);
            familyMemberRepository.save(familyMember);
        }

        kafkaTemplate.send("family-updated",
                userId.toString(),
                familyHead.getFamilyName()+" was updated");
        return "Family updated";
    }

    private void updateFamilyHead(FamilyHead familyHead, UpdateFamilyHeadRequest request) {
        if (request == null) {
            return;
        }
        if (request.getFamilyName() != null) familyHead.setFamilyName(request.getFamilyName());
        if (request.getNumberOfFamilyMembers() != null) familyHead.setNumberOfFamilyMembers(request.getNumberOfFamilyMembers());
        if (request.getMemberShipType() != null) familyHead.setMemberShipType(request.getMemberShipType());
        if (request.getRegistrationCategory() != null) familyHead.setRegistrationCategory(request.getRegistrationCategory());
        if (request.getFirstName() != null) familyHead.setFirstName(request.getFirstName());
        if (request.getLastName() != null) familyHead.setLastName(request.getLastName());
        if (request.getGender() != null) familyHead.setGender(request.getGender());
        if (request.getMaritalStatus() != null) familyHead.setMaritalStatus(request.getMaritalStatus());
        if (request.getBloodGroup() != null) familyHead.setBloodGroup(request.getBloodGroup());
        if (request.getMobileNumber() != null) familyHead.setMobileNumber(request.getMobileNumber());
        if (request.getOccupation() != null) familyHead.setOccupation(request.getOccupation());
        if (request.getProfession() != null) familyHead.setProfession(request.getProfession());
        if (request.getQualification() != null) familyHead.setQualification(request.getQualification());
        if (request.getDesignation() != null) familyHead.setDesignation(request.getDesignation());
        if (request.getAnnualIncome() != null) familyHead.setAnnualIncome(request.getAnnualIncome());
        if (request.getAddressLine1() != null) familyHead.setAddressLine1(request.getAddressLine1());
        if (request.getAddressLine2() != null) familyHead.setAddressLine2(request.getAddressLine2());
        if (request.getCity() != null) familyHead.setCity(request.getCity());
        if (request.getCountry() != null) familyHead.setCountry(request.getCountry());
        if (request.getState() != null) familyHead.setState(request.getState());
        if (request.getPinCode() != null) familyHead.setPincode(request.getPinCode());
        if (request.getPhoto() != null) familyHead.setPhoto(request.getPhoto());
    }

    private void updateFamilyMember(FamilyMember familyMember, UpdateFamilyMemberRequest request) {
        if (request.getRelationShipWithFamilyHead() != null) {
            familyMember.setRelationShipWithFamilyHead(
                    Relation.valueOf(request.getRelationShipWithFamilyHead().trim().toUpperCase())
            );
        }
        if (request.getFirstName() != null) familyMember.setFirstName(request.getFirstName());
        if (request.getLastName() != null) familyMember.setLastName(request.getLastName());
        if (request.getGender() != null) familyMember.setGender(request.getGender());
        if (request.getMaritalStatus() != null) familyMember.setMaritalStatus(request.getMaritalStatus());
        if (request.getBloodGroup() != null) familyMember.setBloodGroup(request.getBloodGroup());
        if (request.getMobileNumber() != null) familyMember.setMobileNumber(request.getMobileNumber());
        if (request.getOccupation() != null) familyMember.setOccupation(request.getOccupation());
        if (request.getEmployment() != null) familyMember.setEmployment(request.getEmployment());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "recent-families",key = "#userId")
    public List<RecentFamilies> recentFamilies(Long userId) {
        List<FamilyHead> familyHeads = familyHeadRepository.findTop5ByUserIdOrderByJoinDateDesc(userId);
        return familyHeads.stream().map(fh->new RecentFamilies(
                fh.getMemberShipId(),
                fh.getFirstName()+" "+fh.getLastName(),
                fh.getNumberOfFamilyMembers(),
                fh.getStatus()
        )).toList();
    }
}
