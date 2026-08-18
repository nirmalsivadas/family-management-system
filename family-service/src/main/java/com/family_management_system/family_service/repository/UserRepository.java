package com.family_management_system.family_service.repository;

import com.family_management_system.family_service.dto.UserResponse;
import com.family_management_system.family_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    UserResponse findByEmail(String email);
}
