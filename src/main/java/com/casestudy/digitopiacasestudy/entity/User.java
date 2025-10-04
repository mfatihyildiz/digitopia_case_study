package com.casestudy.digitopiacasestudy.entity;

import com.casestudy.digitopiacasestudy.enums.UserRole;
import com.casestudy.digitopiacasestudy.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.PENDING;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Full name must contain only letters")
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String normalizedName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @ManyToMany(mappedBy = "users")
    private Set<Organization> organizations = new HashSet<>();

    public void setFullName(String fullName) {
        this.fullName = fullName;
        this.normalizedName = fullName.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
