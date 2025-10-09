package com.casestudy.userservice.repository;

import com.casestudy.userservice.entity.User;
import com.casestudy.userservice.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<User> findByEmailIgnoreCase(String email);
    List<User> findByRole(UserRole role);
}
