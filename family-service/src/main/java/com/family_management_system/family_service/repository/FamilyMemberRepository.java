package com.family_management_system.family_service.repository;

import com.family_management_system.family_service.dto.ViewMembers;
import com.family_management_system.family_service.entity.FamilyMember;
import com.family_management_system.family_service.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember,Long> {
    ViewMembers findAllFamilyMembers();
    Long countByFamilyHeadUserId(Long userId);
    Long countByStatus(Status status);
    Page<FamilyMember> findByFamilyHeadUserId(Long userId, Pageable pageable);
}
