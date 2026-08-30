package com.family_management_system.master_service.repository;

import com.family_management_system.master_service.dto.MasterResponse;
import com.family_management_system.master_service.entity.RegistrationCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationCategoriesRepository extends JpaRepository<RegistrationCategories,Long> {
    @Query(nativeQuery = true, value = "SELECT name from registration_categories")
    List<MasterResponse> findAllNames();
}
