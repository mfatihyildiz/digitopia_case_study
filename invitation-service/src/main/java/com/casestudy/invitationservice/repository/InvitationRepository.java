package com.casestudy.invitationservice.repository;

import com.casestudy.invitationservice.entity.Invitation;
import com.casestudy.invitationservice.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByUserIdAndOrganizationIdAndStatus(UUID userId, UUID organizationId, InvitationStatus status);
    List<Invitation> findByStatus(InvitationStatus status);
    List<Invitation> findByUserId(UUID userId);
    List<Invitation> findByOrganizationId(UUID organizationId);
}
