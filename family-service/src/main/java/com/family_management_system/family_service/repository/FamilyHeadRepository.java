package com.family_management_system.family_service.repository;

import com.family_management_system.family_service.entity.FamilyHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyHeadRepository extends JpaRepository<FamilyHead,Long> {
}
