package com.casestudy.invitationservice.entity;


import com.casestudy.invitationservice.enums.InvitationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invitations", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","organization_id","status"}))
@Getter
@Setter
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private UUID userId;

    @Column(name = "organization_id", nullable = false)
    @NotNull
    private UUID organizationId;

    @NotBlank
    @Column(nullable = false, length = 250)
    private String invitationMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Future
    private LocalDateTime expirationDate;

    @PrePersist
    private void prePersist() {
        if (expirationDate == null) {
            expirationDate = LocalDateTime.now().plusDays(7);
        }
    }
}
