package com.casestudy.digitopiacasestudy.repository;

import com.casestudy.digitopiacasestudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNormalizedName(String normalizedName);
    boolean existsByEmail(String email);
}
