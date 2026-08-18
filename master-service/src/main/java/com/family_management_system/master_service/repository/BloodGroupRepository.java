package com.family_management_system.master_service.repository;

import com.family_management_system.master_service.dto.MasterResponse;
import com.family_management_system.master_service.entity.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {
    @Query(nativeQuery = true, value = "SELECT name from blood_groups")
    List<MasterResponse> findAllNames();
}
