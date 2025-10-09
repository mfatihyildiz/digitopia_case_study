package com.casestudy.userservice.entity;

import com.casestudy.userservice.enums.UserRole;
import com.casestudy.userservice.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_normalized_name", columnList = "normalized_name"),
                @Index(name = "idx_user_email", columnList = "email")
        }
)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Full name cannot be blank")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Full name can only contain letters and spaces")
    private String fullName;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.PENDING;

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
    public void normalizeName() {
        if (fullName != null) {
            this.normalizedName = fullName
                    .trim()
                    .replace("ç", "c").replace("Ç", "C")
                    .replace("ğ", "g").replace("Ğ", "G")
                    .replace("ı", "i").replace("İ", "I")
                    .replace("ö", "o").replace("Ö", "O")
                    .replace("ş", "s").replace("Ş", "S")
                    .replace("ü", "u").replace("Ü", "U")
                    .toLowerCase(Locale.ENGLISH)
                    .replaceAll("[^a-z0-9]", "");
        }
    }
}
