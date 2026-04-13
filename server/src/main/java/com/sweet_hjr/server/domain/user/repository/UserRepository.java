package com.sweet_hjr.server.domain.user.repository;

import com.sweet_hjr.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}