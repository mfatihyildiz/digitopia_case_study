package com.casestudy.organizationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "organizations")
@Getter
@Setter
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String registryNumber;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String organizationName;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String contactEmail;

    @Min(1)
    @Column(nullable = false)
    private Integer companySize;

    @Min(1800)
    @Max(2100)
    private Integer yearFounded;
}
