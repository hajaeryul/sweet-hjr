package com.sweet_hjr.server.domain.user.repository;

import com.sweet_hjr.server.domain.user.entity.RoleType;
import com.sweet_hjr.server.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    boolean existsByUserIdAndRoleType(Long userId, RoleType roleType);
}