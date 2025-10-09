package com.casestudy.organizationservice.repository;

import com.casestudy.organizationservice.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    List<Organization> findByOrganizationNameContainingIgnoreCase(String name);

}
