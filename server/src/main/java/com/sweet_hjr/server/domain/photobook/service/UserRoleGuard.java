package com.sweet_hjr.server.domain.photobook.service;

import com.sweet_hjr.server.domain.user.entity.RoleType;
import com.sweet_hjr.server.domain.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleGuard {

    private final UserRoleRepository userRoleRepository;

    public void assertAdmin(Long userId) {
        boolean isAdmin = userRoleRepository.existsByUserIdAndRoleType(userId, RoleType.ADMIN);
        if (!isAdmin) {
            throw new IllegalStateException("관리자 권한이 필요합니다.");
        }
    }
}