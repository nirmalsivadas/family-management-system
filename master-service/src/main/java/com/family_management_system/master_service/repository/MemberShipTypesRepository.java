package com.family_management_system.master_service.repository;

import com.family_management_system.master_service.dto.MasterResponse;
import com.family_management_system.master_service.entity.MemberShipTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberShipTypesRepository extends JpaRepository<MemberShipTypes,Long> {
    @Query(nativeQuery = true, value = "SELECT name from membership_types")
    List<MasterResponse> findAllNames();
}
