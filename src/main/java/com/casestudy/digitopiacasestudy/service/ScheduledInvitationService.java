package com.casestudy.digitopiacasestudy.service;

import com.casestudy.digitopiacasestudy.entity.Invitation;
import com.casestudy.digitopiacasestudy.enums.InvitationStatus;
import com.casestudy.digitopiacasestudy.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledInvitationService {

    private final InvitationRepository invitationRepository;

    /**
     * Scheduled job that runs daily at midnight to expire old invitations
     * This meets the case study requirement: "Invitations automatically expire
     * after 7 days"
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void expireOldInvitations() {
        log.info("Starting scheduled job to expire old invitations");

        List<Invitation> pendingInvitations = invitationRepository.findByStatus(InvitationStatus.PENDING);
        LocalDateTime now = LocalDateTime.now();
        int expiredCount = 0;

        for (Invitation invitation : pendingInvitations) {
            if (invitation.getCreatedAt().plusDays(7).isBefore(now)) {
                invitation.setStatus(InvitationStatus.EXPIRED);
                invitationRepository.save(invitation);
                expiredCount++;
                log.debug("Expired invitation ID: {} for user: {} and organization: {}",
                        invitation.getId(),
                        invitation.getUser().getId(),
                        invitation.getOrganization().getId());
            }
        }

        log.info("Scheduled job completed. Expired {} invitations", expiredCount);
    }

    /**
     * Manual method to expire invitations (for testing purposes)
     * This is the same method that was already in InvitationService
     */
    @Transactional
    public void expireOldInvitationsManual() {
        log.info("Manually expiring old invitations");
        expireOldInvitations();
    }
}
