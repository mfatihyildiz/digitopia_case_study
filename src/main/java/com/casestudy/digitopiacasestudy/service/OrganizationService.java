package com.casestudy.digitopiacasestudy.service;

import com.casestudy.digitopiacasestudy.entity.Organization;
import com.casestudy.digitopiacasestudy.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization getById(UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with ID: " + id));
    }

    public Organization getByRegistryNumber(String registryNumber) {
        return organizationRepository.findByRegistryNumber(registryNumber)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with registry number: " + registryNumber));
    }

    public Organization createOrganization(Organization organization) {
        if (organizationRepository.findByRegistryNumber(organization.getRegistryNumber()).isPresent()) {
            throw new IllegalArgumentException("Registry number already exists: " + organization.getRegistryNumber());
        }
        return organizationRepository.save(organization);
    }

    public Organization updateOrganization(UUID id, Organization updated) {
        Organization org = getById(id);
        org.setOrganizationName(updated.getOrganizationName());
        org.setContactEmail(updated.getContactEmail());
        org.setCompanySize(updated.getCompanySize());
        org.setYearFounded(updated.getYearFounded());
        return organizationRepository.save(org);
    }

    public void deleteOrganization(UUID id) {
        organizationRepository.deleteById(id);
    }

    public List<Organization> searchOrganizations(String name, Integer year, Integer size) {
        if (name != null)
            return organizationRepository.findByNormalizedOrganizationNameContainingIgnoreCase(name);
        else if (year != null && size != null)
            return organizationRepository.findByYearFoundedAndCompanySize(year, size);
        else
            return getAllOrganizations();
    }
}
