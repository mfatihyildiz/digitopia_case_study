package com.casestudy.organizationservice.service;

import com.casestudy.organizationservice.entity.Organization;
import com.casestudy.organizationservice.entity.OrganizationMember;
import com.casestudy.organizationservice.repository.OrganizationMemberRepository;
import com.casestudy.organizationservice.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationMemberService {

    private final OrganizationMemberRepository orgMemRepo;
    private final OrganizationRepository orgRepo;

    @Transactional
    public void addMember(UUID orgId, UUID userId) {
        if (orgMemRepo.existsByOrganizationIdAndUserId(orgId, userId)) {
            throw new IllegalArgumentException("User is already a member of this organization.");
        }

        Organization organization = orgRepo.findById(orgId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + orgId));

        long currentCount = orgMemRepo.countByOrganizationId(orgId);

        if (currentCount >= organization.getCompanySize()) {
            throw new IllegalStateException(String.format(
                    "Organization '%s' has reached its member limit (%d members).",
                    organization.getOrganizationName(),
                    organization.getCompanySize()
            ));
        }

        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(orgId);
        member.setUserId(userId);
        orgMemRepo.save(member);
    }

    @Transactional(readOnly = true)
    public List<UUID> listUserIds(UUID orgId) {
        return orgMemRepo.findByOrganizationId(orgId)
                .stream()
                .map(OrganizationMember::getUserId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeMember(UUID orgId, UUID userId) {
        boolean exists = orgMemRepo.existsByOrganizationIdAndUserId(orgId, userId);
        if (!exists) {
            throw new EntityNotFoundException(
                    String.format("User %s not found in organization %s", userId, orgId)
            );
        }
        orgMemRepo.deleteByOrganizationIdAndUserId(orgId, userId);
    }
}
