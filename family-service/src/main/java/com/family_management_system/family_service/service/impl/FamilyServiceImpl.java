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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
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
    @Transactional
    @CacheEvict(value = {
            "view-families",
            "view-members",
            "view-family",
            "recent-families",
            "total-families",
            "total-members",
            "families-with-status"
    }, allEntries = true)
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
        familyHead.setMemberShipId(nextMembershipNumber());
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
    @Cacheable(value = "view-families",key = "#userId + ':' + #status + ':' + #page + ':' + #size + ':' + #query")
    public Page<ViewFamilies> viewFamilies(Long userId,String status, int page, int size, String query) {
        Pageable pageable = PageRequest.of(page, size);

        Status statusEnum = null;
        if (status != null && !status.trim().isEmpty() && !status.trim().equalsIgnoreCase("ALL")) {
            statusEnum = Status.valueOf(status.trim().toUpperCase());
        }

        String normalizedQuery = query == null ? "" : query.trim().toLowerCase();
        Status finalStatusEnum = statusEnum;
        List<FamilyHead> matchedFamilies = familyHeadRepository.findListByUserId(userId)
                .stream()
                .filter(familyHead -> finalStatusEnum == null || familyHead.getStatus() == finalStatusEnum)
                .filter(familyHead -> normalizedQuery.isBlank() || familyMatches(familyHead, normalizedQuery))
                .toList();

        int start = Math.min((int) pageable.getOffset(), matchedFamilies.size());
        int end = Math.min(start + pageable.getPageSize(), matchedFamilies.size());
        Page<FamilyHead> familyHeads = new PageImpl<>(matchedFamilies.subList(start, end), pageable, matchedFamilies.size());

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
    @Cacheable(value = "view-members",key = "#userId + ':' + #page + ':' + #size + ':' + #query")
    public Page<ViewMembers> viewMembers(Long userId,int page,int size, String query) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").descending());

        List<FamilyHead> familyHeads = familyHeadRepository.searchListByUser(userId, query);
        List<FamilyMember> familyMembers = familyMemberRepository.searchListByUser(userId, query);
        List<ViewMembers> memberRows = new ArrayList<>();

        for (FamilyHead familyHead : familyHeads) {
            memberRows.add(new ViewMembers(
                    "HEAD-" + familyHead.getId(),
                    fullName(familyHead.getFirstName(), familyHead.getLastName()),
                    "FAMILY_HEAD",
                    familyHead.getFamilyName(),
                    familyHead.getMemberShipId(),
                    familyHead.getOccupation(),
                    familyHead.getMobileNumber(),
                    familyHead.getStatus()
            ));
        }

        for (FamilyMember member : familyMembers) {
            FamilyHead familyHead = member.getFamilyHead();
            if (familyHead == null) {
                continue;
            }
            memberRows.add(new ViewMembers(
                    "MEMBER-" + member.getId(),
                    fullName(member.getFirstName(), member.getLastName()),
                    member.getRelationShipWithFamilyHead().name(),
                    familyHead.getFamilyName(),
                    familyHead.getMemberShipId(),
                    member.getOccupation(),
                    member.getMobileNumber(),
                    member.getStatus()
            ));
        }

        int start = Math.min((int) pageable.getOffset(), memberRows.size());
        int end = Math.min(start + pageable.getPageSize(), memberRows.size());
        return new PageImpl<>(memberRows.subList(start, end), pageable, memberRows.size());
}

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "view-family",key = "#userId + '_' + #memberShipId")
    public ViewFamily viewFamily(Long userId,String memberShipId) {
        FamilyHead familyHead = familyHeadRepository.findByUserIdAndMemberShipId(userId,memberShipId);
        if (familyHead == null) {
            throw new RuntimeException("Family not found");
        }

        ViewFamily viewFamily = new ViewFamily();
        viewFamily.setMembershipId(familyHead.getMemberShipId());
        viewFamily.setFamilyName(familyHead.getFamilyName());
        viewFamily.setRegistrationDate(familyHead.getJoinDate());
        viewFamily.setNumberOfFamilyMembers(familyHead.getNumberOfFamilyMembers());
        viewFamily.setFamilyMemberShipType(familyHead.getMemberShipType());
        viewFamily.setStatus(familyHead.getStatus().name());
        viewFamily.setRegistrationCategory(familyHead.getRegistrationCategory());
        viewFamily.setAddress(joinAddress(familyHead.getAddressLine1(), familyHead.getAddressLine2()));
        viewFamily.setAddressLine1(familyHead.getAddressLine1());
        viewFamily.setAddressLine2(familyHead.getAddressLine2());
        viewFamily.setCity(familyHead.getCity());
        viewFamily.setState(familyHead.getState());
        viewFamily.setCountry(familyHead.getCountry());
        viewFamily.setPinCode(familyHead.getPincode());
        viewFamily.setFirstName(familyHead.getFirstName());
        viewFamily.setMiddleName(familyHead.getMiddleName());
        viewFamily.setLastName(familyHead.getLastName());
        viewFamily.setMobileNumber(familyHead.getMobileNumber());
        viewFamily.setAlternateMobile(familyHead.getAlternateMobile());
        viewFamily.setFamilyHeadGender(familyHead.getGender());
        viewFamily.setDateOfBirth(familyHead.getDateOfBirth());
        viewFamily.setMaritalStatus(familyHead.getMaritalStatus());
        viewFamily.setFamilHeadName(familyHead.getFirstName()+" "+familyHead.getLastName());
        viewFamily.setBloodGroup(familyHead.getBloodGroup());
        viewFamily.setEmail(familyHead.getEmail());
        viewFamily.setOccupation(familyHead.getOccupation());
        viewFamily.setEmployment(familyHead.getEmployment());
        viewFamily.setDesignation(familyHead.getDesignation());
        viewFamily.setQualification(familyHead.getQualification());
        viewFamily.setProfession(familyHead.getProfession());
        viewFamily.setAnnualIncome(familyHead.getAnnualIncome());
        viewFamily.setOrganization(familyHead.getOrganization());
        if (familyHead.getPhoto()!=null && familyHead.getPhoto().length>0){
            String encodedPhoto = Base64.getEncoder().encodeToString(familyHead.getPhoto());
            viewFamily.setPhoto(encodedPhoto);
        }
        List<FamilyMember> familyMembers = familyMemberRepository
                .findByFamilyHeadMemberShipId(memberShipId);
        viewFamily.setFamilyMembers(familyMembers.stream().map(member -> new FamilyMemberView(
                member.getId(),
                member.getRelationShipWithFamilyHead(),
                member.getFirstName(),
                member.getMiddleName(),
                member.getLastName(),
                member.getDateOfBirth(),
                member.getGender(),
                member.getMaritalStatus(),
                member.getBloodGroup(),
                member.getMobileNumber(),
                member.getEmail(),
                member.getOccupation(),
                member.getEmployment(),
                member.getStatus()
        )).toList());
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

        List<UpdateFamilyMemberRequest> memberRequests = new ArrayList<>();
        if (updateFamilyRequest.getUpdateFamilyMemberRequests() != null) {
            memberRequests.addAll(updateFamilyRequest.getUpdateFamilyMemberRequests());
        }
        if (updateFamilyRequest.getUpdateFamilyMemberRequest() != null) {
            memberRequests.add(updateFamilyRequest.getUpdateFamilyMemberRequest());
        }

        for (UpdateFamilyMemberRequest memberRequest : memberRequests) {
            if (memberRequest == null || memberRequest.getFamilyMemberId() == null) {
                continue;
            }
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

    @Override
    @Transactional
    @CacheEvict(value = {
            "view-families",
            "view-members",
            "view-family",
            "recent-families",
            "families-with-status"
    }, allEntries = true)
    public String changeStatus(Long userId, String memberShipId, String status) {
        FamilyHead familyHead = familyHeadRepository.findByUserIdAndMemberShipId(userId, memberShipId);
        if (familyHead == null) {
            throw new RuntimeException("Family not found");
        }
        Status nextStatus = Status.valueOf(status.trim().toUpperCase());
        familyHead.setStatus(nextStatus);
        familyHeadRepository.save(familyHead);

        List<FamilyMember> familyMembers = familyMemberRepository.findByFamilyHeadMemberShipId(memberShipId);
        for (FamilyMember familyMember : familyMembers) {
            familyMember.setStatus(nextStatus);
        }
        if (!familyMembers.isEmpty()) {
            familyMemberRepository.saveAll(familyMembers);
        }

        kafkaTemplate.send("status-changed",
                userId.toString(),
                familyHead.getFamilyName()+" status changed to "+nextStatus.name());
        return "Status changed";
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
        if (request.getMiddleName() != null) familyHead.setMiddleName(request.getMiddleName());
        if (request.getLastName() != null) familyHead.setLastName(request.getLastName());
        if (request.getDateOfBirth() != null) familyHead.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) familyHead.setGender(request.getGender());
        if (request.getMaritalStatus() != null) familyHead.setMaritalStatus(request.getMaritalStatus());
        if (request.getBloodGroup() != null) familyHead.setBloodGroup(request.getBloodGroup());
        if (request.getMobileNumber() != null) familyHead.setMobileNumber(request.getMobileNumber());
        if (request.getAlternateMobile() != null) familyHead.setAlternateMobile(request.getAlternateMobile());
        if (request.getEmail() != null) familyHead.setEmail(request.getEmail());
        if (request.getOccupation() != null) familyHead.setOccupation(request.getOccupation());
        if (request.getEmployment() != null) familyHead.setEmployment(request.getEmployment());
        if (request.getProfession() != null) familyHead.setProfession(request.getProfession());
        if (request.getQualification() != null) familyHead.setQualification(request.getQualification());
        if (request.getDesignation() != null) familyHead.setDesignation(request.getDesignation());
        if (request.getOrganization() != null) familyHead.setOrganization(request.getOrganization());
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

    private String nextMembershipNumber() {
        long next = familyHeadRepository.count() + 1;
        return String.format("FM-%d-%06d", Year.now().getValue(), next);
    }

    private String joinAddress(String line1, String line2) {
        if (line1 == null) {
            return line2;
        }
        if (line2 == null || line2.isBlank()) {
            return line1;
        }
        return line1 + " " + line2;
    }

    private String fullName(String firstName, String lastName) {
        List<String> names = new ArrayList<>();
        if (firstName != null && !firstName.isBlank()) {
            names.add(firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            names.add(lastName);
        }
        return String.join(" ", names);
    }

    private boolean familyMatches(FamilyHead familyHead, String query) {
        return containsIgnoreCase(familyHead.getMemberShipId(), query)
                || containsIgnoreCase(familyHead.getFamilyName(), query)
                || containsIgnoreCase(familyHead.getFirstName(), query)
                || containsIgnoreCase(familyHead.getLastName(), query)
                || containsIgnoreCase(fullName(familyHead.getFirstName(), familyHead.getLastName()), query)
                || containsIgnoreCase(familyHead.getStatus() != null ? familyHead.getStatus().name() : null, query);
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }
}
