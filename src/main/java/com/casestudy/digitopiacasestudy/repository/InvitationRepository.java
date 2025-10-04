package com.casestudy.digitopiacasestudy.repository;

import com.casestudy.digitopiacasestudy.entity.Invitation;
import com.casestudy.digitopiacasestudy.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    List<Invitation> findByStatus(InvitationStatus status);
    Optional<Invitation> findByUserIdAndOrganizationIdAndStatus(UUID userId, UUID organizationId, InvitationStatus status);
}
