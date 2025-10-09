package com.casestudy.invitationservice.service;

import com.casestudy.invitationservice.entity.Invitation;
import com.casestudy.invitationservice.enums.InvitationStatus;
import com.casestudy.invitationservice.repository.InvitationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
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
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found: " + id));
    }

    public Invitation create(Invitation inv) {
        repo.findByUserIdAndOrganizationIdAndStatus(inv.getUserId(), inv.getOrganizationId(), InvitationStatus.PENDING)
                .ifPresent(x -> {
                    throw new IllegalArgumentException("Pending invitation already exists for this user & organization");
                });

        inv.setStatus(InvitationStatus.PENDING);
        if (inv.getExpirationDate() == null) {
            inv.setExpirationDate(LocalDateTime.now().plusDays(7));
        }
        return repo.save(inv);
    }

    public Invitation updateStatus(UUID id, InvitationStatus status) {
        var inv = getById(id);
        inv.setStatus(status);
        var saved = repo.save(inv);

        if (status == InvitationStatus.ACCEPTED) {
            notifyOrganizationService(inv);
        }

        return saved;
    }

    public void expireOld() {
        var pending = repo.findByStatus(InvitationStatus.PENDING);
        var now = LocalDateTime.now();
        pending.stream()
                .filter(i -> i.getExpirationDate() != null && i.getExpirationDate().isBefore(now))
                .forEach(i -> {
                    i.setStatus(InvitationStatus.EXPIRED);
                    repo.save(i);
                });
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
