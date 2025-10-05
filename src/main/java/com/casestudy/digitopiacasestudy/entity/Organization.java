package com.casestudy.digitopiacasestudy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations", indexes = {
        @Index(name = "idx_org_registry_number", columnList = "registryNumber"),
        @Index(name = "idx_org_normalized_name", columnList = "normalizedOrganizationName"),
        @Index(name = "idx_org_year_size", columnList = "yearFounded, companySize")
})
@Getter
@Setter
public class Organization extends BaseEntity {

    @NotBlank(message = "Registry number cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Registry number must be alphanumeric")
    @Column(nullable = false, unique = true)
    private String registryNumber;

    @NotBlank(message = "Organization name cannot be blank")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String organizationName;

    @Column(nullable = false)
    private String normalizedOrganizationName;

    @NotBlank(message = "Contact email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(nullable = false)
    private String contactEmail;

    @Min(value = 1, message = "Company size must be at least 1")
    @Max(value = 1000000, message = "Company size cannot exceed 1,000,000")
    private Integer companySize;

    @Min(value = 1800, message = "Year founded cannot be before 1800")
    @Max(value = 2100, message = "Year founded cannot be after 2100")
    private Integer yearFounded;

    @ManyToMany
    @JoinTable(name = "organization_users", joinColumns = @JoinColumn(name = "organization_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        this.normalizedOrganizationName = organizationName.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
