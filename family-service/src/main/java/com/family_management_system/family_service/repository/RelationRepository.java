package com.family_management_system.family_service.repository;

import com.family_management_system.family_service.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<Relation,Long> {
}
