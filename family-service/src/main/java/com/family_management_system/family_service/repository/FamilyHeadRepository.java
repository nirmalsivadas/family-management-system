package com.family_management_system.family_service.repository;
import com.family_management_system.family_service.entity.FamilyHead;
import com.family_management_system.family_service.dto.RecentFamilies;
import com.family_management_system.family_service.dto.ViewFamilies;
import com.family_management_system.family_service.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyHeadRepository extends JpaRepository<FamilyHead,Long> {
    Long countByUserId(Long userId);
    Long countByUserIdAndStatus(Long userId, Status status);
    FamilyHead findByUserIdAndMemberShipId(Long userId,String memberShipId);
    List<FamilyHead> findTop5ByUserIdOrderByJoinDateDesc(Long userId);
    FamilyHead findByUserId(Long userId);

    @Query("""
            SELECT f FROM FamilyHead f
            WHERE f.user.id = :userId
            ORDER BY f.joinDate DESC
            """)
    List<FamilyHead> findListByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT f FROM FamilyHead f
            WHERE f.user.id = :userId
            AND (:status IS NULL OR f.status = :status)
            AND (
                :q IS NULL OR TRIM(:q) = ''
                OR LOWER(f.familyName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.memberShipId) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            """)
    Page<FamilyHead> searchByUser(
            @Param("userId") Long userId,
            @Param("status") Status status,
            @Param("q") String q,
            Pageable pageable
    );

    @Query(value = """
            SELECT new com.family_management_system.family_service.dto.ViewFamilies(
                f.memberShipId,
                CONCAT(f.firstName, ' ', f.lastName),
                f.familyName,
                f.numberOfFamilyMembers,
                f.joinDate,
                f.status
            )
            FROM FamilyHead f
            WHERE f.user.id = :userId
            AND (:status IS NULL OR f.status = :status)
            AND (
                :q IS NULL OR TRIM(:q) = ''
                OR LOWER(f.familyName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.memberShipId) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(CONCAT(f.firstName, ' ', f.lastName)) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            ORDER BY f.joinDate DESC
            """,
            countQuery = """
            SELECT COUNT(f)
            FROM FamilyHead f
            WHERE f.user.id = :userId
            AND (:status IS NULL OR f.status = :status)
            AND (
                :q IS NULL OR TRIM(:q) = ''
                OR LOWER(f.familyName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.memberShipId) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(CONCAT(f.firstName, ' ', f.lastName)) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            """)
    Page<ViewFamilies> searchViewFamiliesByUser(
            @Param("userId") Long userId,
            @Param("status") Status status,
            @Param("q") String q,
            Pageable pageable
    );

    @Query("""
            SELECT f FROM FamilyHead f
            WHERE f.user.id = :userId
            AND (
                :q IS NULL OR TRIM(:q) = ''
                OR LOWER(f.familyName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.memberShipId) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.firstName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(f.lastName) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            ORDER BY f.joinDate DESC
            """)
    List<FamilyHead> searchListByUser(
            @Param("userId") Long userId,
            @Param("q") String q
    );

    @Query("""
            SELECT new com.family_management_system.family_service.dto.RecentFamilies(
                f.memberShipId,
                CONCAT(f.firstName, ' ', f.lastName),
                f.numberOfFamilyMembers,
                f.status
            )
            FROM FamilyHead f
            WHERE f.user.id = :userId
            ORDER BY f.joinDate DESC
            """)
    List<RecentFamilies> findRecentFamilyViewsByUserId(@Param("userId") Long userId, Pageable pageable);
}
