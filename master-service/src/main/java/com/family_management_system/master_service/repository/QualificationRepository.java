package com.family_management_system.master_service.repository;

import com.family_management_system.master_service.dto.MasterResponse;
import com.family_management_system.master_service.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    @Query(nativeQuery = true, value = "SELECT name from qualifications")
    List<MasterResponse> findAllNames();
}
