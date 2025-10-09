package com.casestudy.organizationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "organizations",
        indexes = {
                @Index(name = "idx_org_registry_number", columnList = "registry_number"),
                @Index(name = "idx_org_normalized_name", columnList = "normalized_organization_name")
        }
)
@Getter
@Setter
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Registry number is required")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Registry number can only contain uppercase letters, digits, and hyphens")
    @Column(name = "registry_number", nullable = false, unique = true)
    private String registryNumber;

    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "normalized_organization_name", nullable = false)
    private String normalizedOrganizationName;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @NotNull(message = "Company size is required")
    @Min(value = 1, message = "Company size must be at least 1")
    @Column(name = "company_size", nullable = false)
    private Integer companySize;

    @NotNull(message = "Year founded is required")
    @Min(value = 1800, message = "Year must be after 1800")
    @Max(value = 2100, message = "Year must be before 2100")
    @Column(name = "year_founded")
    private Integer yearFounded;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @PrePersist
    @PreUpdate
    public void normalizeOrganizationName() {
        if (organizationName != null) {
            this.normalizedOrganizationName = organizationName
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]", "");
        }
    }
}
