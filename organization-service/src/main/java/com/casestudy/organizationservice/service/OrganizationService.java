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

    @Transactional
    public Organization update(UUID id, Organization updated) {
        Organization existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found: " + id));

        existing.setOrganizationName(updated.getOrganizationName());
        existing.setRegistryNumber(updated.getRegistryNumber());
        existing.setContactEmail(updated.getContactEmail());
        existing.setCompanySize(updated.getCompanySize());
        existing.setYearFounded(updated.getYearFounded());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<Organization> searchByName(String name) {
        return repository.findByOrganizationNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Organization> getByUserId(UUID userId) {
        List<UUID> orgIds = memberRepository.findByUserId(userId)
                .stream()
                .map(OrganizationMember::getOrganizationId)
                .toList();
        return repository.findAllById(orgIds);
    }
}
