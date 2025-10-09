package com.casestudy.organizationservice.service;

import com.casestudy.organizationservice.entity.Organization;
import com.casestudy.organizationservice.entity.OrganizationMember;
import com.casestudy.organizationservice.repository.OrganizationMemberRepository;
import com.casestudy.organizationservice.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;
    private final OrganizationMemberRepository memberRepository;

    public List<Organization> getAll() {
        return repository.findAll();
    }

    public Organization getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + id));
    }

    public Organization create(Organization organization) {
        return repository.save(organization);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public void addMember(UUID orgId, UUID userId) {
        var org = repository.findById(orgId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + orgId));

        boolean exists = memberRepository.existsByOrganizationIdAndUserId(orgId, userId);
        if (!exists) {
            OrganizationMember member = new OrganizationMember();
            member.setOrganizationId(orgId);
            member.setUserId(userId);
            memberRepository.save(member);
        }
    }

    public void removeMember(UUID orgId, UUID userId) {
        memberRepository.deleteByOrganizationIdAndUserId(orgId, userId);
    }

    public List<OrganizationMember> listMembers(UUID orgId) {
        return memberRepository.findByOrganizationId(orgId);
    }
}
