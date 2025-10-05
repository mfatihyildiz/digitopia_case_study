package com.casestudy.digitopiacasestudy.entity;

import com.casestudy.digitopiacasestudy.enums.InvitationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitations", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "organization_id",
        "status" }), indexes = {
        @Index(name = "idx_invitation_status", columnList = "status"),
        @Index(name = "idx_invitation_expiration", columnList = "expirationDate"),
        @Index(name = "idx_invitation_user_org", columnList = "user_id, organization_id")
})
@Getter
@Setter
public class Invitation extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    @NotNull(message = "Organization cannot be null")
    private Organization organization;

    @NotBlank(message = "Invitation message cannot be blank")
    @Column(nullable = false, length = 250)
    private String invitationMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Future(message = "Expiration date must be in the future")
    @Column(nullable = false)
    private LocalDateTime expirationDate = LocalDateTime.now().plusDays(7);
}
