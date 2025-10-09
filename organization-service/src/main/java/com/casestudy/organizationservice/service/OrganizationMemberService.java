package com.casestudy.organizationservice.service;

import com.casestudy.organizationservice.entity.OrganizationMember;
import com.casestudy.organizationservice.repository.OrganizationMemberRepository;
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

    private final OrganizationMemberRepository repo;

    @Transactional
    public void addMember(UUID orgId, UUID userId) {
        boolean exists = repo.existsByOrganizationIdAndUserId(orgId, userId);
        if (exists) {
            return;
        }

        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(orgId);
        member.setUserId(userId);
        repo.save(member);
    }

    @Transactional(readOnly = true)
    public List<UUID> listUserIds(UUID orgId) {
        return repo.findByOrganizationId(orgId)
                .stream()
                .map(OrganizationMember::getUserId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeMember(UUID orgId, UUID userId) {
        boolean exists = repo.existsByOrganizationIdAndUserId(orgId, userId);
        if (!exists) {
            throw new EntityNotFoundException(
                    String.format("User %s not found in organization %s", userId, orgId)
            );
        }
        repo.deleteByOrganizationIdAndUserId(orgId, userId);
    }
}
