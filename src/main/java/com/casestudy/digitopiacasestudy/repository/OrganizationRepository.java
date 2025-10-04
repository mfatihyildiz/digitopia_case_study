package com.casestudy.digitopiacasestudy.repository;

import com.casestudy.digitopiacasestudy.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findByRegistryNumber(String registryNumber);
    List<Organization> findByNormalizedOrganizationNameContainingIgnoreCase(String name);
    List<Organization> findByYearFoundedAndCompanySize(Integer yearFounded, Integer companySize);
}
