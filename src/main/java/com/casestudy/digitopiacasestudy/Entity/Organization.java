package com.casestudy.digitopiacasestudy.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String registryNumber;

    @NotBlank
    @Column(nullable = false)
    private String organizationName;

    @Column(nullable = false)
    private String normalizedOrganizationName;

    @Email
    @Column(nullable = false)
    private String contactEmail;

    private Integer companySize;
    private Integer yearFounded;

    @ManyToMany
    @JoinTable(
            name = "organization_users",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalizeName() {
        this.normalizedOrganizationName = organizationName.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
