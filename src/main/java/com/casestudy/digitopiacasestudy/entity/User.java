package com.casestudy.digitopiacasestudy.entity;

import com.casestudy.digitopiacasestudy.enums.UserRole;
import com.casestudy.digitopiacasestudy.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.PENDING;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Full name must contain only letters")
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String normalizedName;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    // Relationships
    @ManyToMany(mappedBy = "users")
    private Set<Organization> organizations = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalizeName() {
        this.normalizedName = fullName.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
