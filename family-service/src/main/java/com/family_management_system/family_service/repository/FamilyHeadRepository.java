package com.family_management_system.family_service.repository;
import com.family_management_system.family_service.entity.FamilyHead;
import com.family_management_system.family_service.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyHeadRepository extends JpaRepository<FamilyHead,Long> {
    Long countByUserId(Long userId);
    Page<FamilyHead> findByUserId(Long userId, Pageable pageable);
    Page<FamilyHead> findByUserIdAndStatus
            (Long userId, Status status, Pageable pageable);
    FamilyHead findByUserIdAndMemberShipId(Long userId,String memberShipId);
    List<FamilyHead> findTop5ByUserIdOrderByJoinDateDesc(Long userId);
    FamilyHead findByUserId(Long userId);
}
