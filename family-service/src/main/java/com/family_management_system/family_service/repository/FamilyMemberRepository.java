package com.family_management_system.family_service.repository;

import com.family_management_system.family_service.entity.FamilyMember;
import com.family_management_system.family_service.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember,Long> {
    Long countByFamilyHeadUserId(Long userId);
    Long countByStatus(Status status);
    Page<FamilyMember> findByFamilyHeadUserId(Long userId, Pageable pageable);
    List<FamilyMember> findByFamilyHeadMemberShipId(String memberShipId);
    Page<FamilyMember> findByFamilyHeadUserIdOrderByFamilyHeadJoinDateDesc(Long userId, Pageable pageable);
    Optional<FamilyMember> findByIdAndFamilyHeadUserIdAndFamilyHeadMemberShipId(
            Long id,
            Long userId,
            String memberShipId
    );

    @Query("""
            SELECT m FROM FamilyMember m
            WHERE m.familyHead.user.id = :userId
            AND (
                :q IS NULL OR TRIM(:q) = ''
                OR LOWER(m.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.familyHead.familyName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.familyHead.memberShipId) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            """)
    Page<FamilyMember> searchByUser(
            @Param("userId") Long userId,
            @Param("q") String q,
            Pageable pageable
    );

    @Query("""
            SELECT m FROM FamilyMember m
            WHERE m.familyHead.user.id = :userId
            AND (
                :q IS NULL OR TRIM(:q) = ''
                OR LOWER(m.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.familyHead.familyName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(m.familyHead.memberShipId) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            ORDER BY m.familyHead.joinDate DESC, m.id DESC
            """)
    List<FamilyMember> searchListByUser(
            @Param("userId") Long userId,
            @Param("q") String q
    );
}
