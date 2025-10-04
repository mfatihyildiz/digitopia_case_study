package com.casestudy.digitopiacasestudy.service;

import com.casestudy.digitopiacasestudy.entity.Invitation;
import com.casestudy.digitopiacasestudy.enums.InvitationStatus;
import com.casestudy.digitopiacasestudy.repository.InvitationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;

    public List<Invitation> getAllInvitations() {
        return invitationRepository.findAll();
    }

    public Invitation getById(UUID id) {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found with ID: " + id));
    }

    public Invitation createInvitation(Invitation invitation) {
        var existing = invitationRepository
                .findByUserIdAndOrganizationIdAndStatus(
                        invitation.getUser().getId(),
                        invitation.getOrganization().getId(),
                        InvitationStatus.PENDING);

        if (existing.isPresent())
            throw new IllegalArgumentException("An active pending invitation already exists.");

        var rejected = invitationRepository
                .findByUserIdAndOrganizationIdAndStatus(
                        invitation.getUser().getId(),
                        invitation.getOrganization().getId(),
                        InvitationStatus.REJECTED);

        if (rejected.isPresent())
            throw new IllegalArgumentException("User previously rejected the invitation and cannot be reinvited.");

        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setExpirationDate(LocalDateTime.now().plusDays(7));

        return invitationRepository.save(invitation);
    }

    public Invitation updateStatus(UUID id, InvitationStatus newStatus) {
        Invitation invitation = getById(id);
        invitation.setStatus(newStatus);
        return invitationRepository.save(invitation);
    }

    public void expireOldInvitations() {
        List<Invitation> pending = invitationRepository.findByStatus(InvitationStatus.PENDING);
        LocalDateTime now = LocalDateTime.now();

        for (Invitation inv : pending) {
            if (inv.getExpirationDate().isBefore(now)) {
                inv.setStatus(InvitationStatus.EXPIRED);
                invitationRepository.save(inv);
            }
        }
    }
}
