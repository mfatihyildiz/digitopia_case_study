package com.casestudy.invitationservice.entity;

import com.casestudy.invitationservice.enums.InvitationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "invitations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "organization_id", "status"}),
        indexes = {
                @Index(name = "idx_invitation_user_id", columnList = "user_id"),
                @Index(name = "idx_invitation_org_id", columnList = "organization_id"),
                @Index(name = "idx_invitation_status", columnList = "status")
        }
)
@Getter
@Setter
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull(message = "Organization ID cannot be null")
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @NotBlank(message = "Invitation message cannot be blank")
    @Column(name = "invitation_message", nullable = false, length = 250)
    private String invitationMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Future(message = "Expiration date must be in the future")
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @PrePersist
    private void prePersist() {
        if (expirationDate == null) {
            expirationDate = LocalDateTime.now().plusDays(7);
        }
    }
}
