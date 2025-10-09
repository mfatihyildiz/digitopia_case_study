package com.casestudy.invitationservice.service;

import com.casestudy.invitationservice.entity.Invitation;
import com.casestudy.invitationservice.enums.InvitationStatus;
import com.casestudy.invitationservice.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationService {

    private final InvitationRepository repo;
    private final RestClient.Builder restClientBuilder;

    @Value("${services.organization.base-url}")
    private String organizationBaseUrl;

    public List<Invitation> getAll() {
        return repo.findAll();
    }

    public Invitation getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found: " + id));
    }

    public List<Invitation> getByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }

    public List<Invitation> getByOrganizationId(UUID orgId) {
        return repo.findByOrganizationId(orgId);
    }

    @Transactional
    public Invitation create(Invitation inv) {
        repo.findByUserIdAndOrganizationIdAndStatus(inv.getUserId(), inv.getOrganizationId(), InvitationStatus.PENDING)
                .ifPresent(x -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "A pending invitation already exists for this user and organization.");
                });

        inv.setStatus(InvitationStatus.PENDING);
        if (inv.getExpirationDate() == null) {
            inv.setExpirationDate(LocalDateTime.now().plusDays(7));
        }
        return repo.save(inv);
    }

    @Transactional
    public Invitation updateStatus(UUID id, InvitationStatus status) {
        Invitation inv = getById(id);
        inv.setStatus(status);
        Invitation saved = repo.save(inv);

        if (status == InvitationStatus.ACCEPTED) {
            notifyOrganizationService(inv);
        }
        return saved;
    }

    @Transactional
    public void delete(UUID id) {
        Invitation inv = getById(id);
        repo.delete(inv);
    }

    @Transactional
    public void expireOld() {
        var pending = repo.findByStatus(InvitationStatus.PENDING);
        var now = LocalDateTime.now();

        pending.stream()
                .filter(i -> i.getExpirationDate() != null && i.getExpirationDate().isBefore(now))
                .forEach(i -> {
                    i.setStatus(InvitationStatus.EXPIRED);
                    repo.save(i);
                });

        log.info("Expired invitations cleanup completed at {}", now);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledExpireOldInvitations() {
        expireOld();
    }

    private void notifyOrganizationService(Invitation inv) {
        RestClient restClient = restClientBuilder.build();
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl(organizationBaseUrl)
                    .path("/api/organizations/{orgId}/members")
                    .queryParam("userId", inv.getUserId())
                    .build(inv.getOrganizationId());

            restClient.post()
                    .uri(uri)
                    .retrieve()
                    .toBodilessEntity();

            log.info("User {} added to organization {} successfully.", inv.getUserId(), inv.getOrganizationId());
        } catch (RestClientException e) {
            log.error("Failed to call organization-service: {}", e.getMessage());
        }
    }
}
