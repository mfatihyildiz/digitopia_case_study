package com.casestudy.digitopiacasestudy.Entity;

import com.casestudy.digitopiacasestudy.Enum.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "invitations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "organization_id", "status"}))
@Getter
@Setter
public class Invitation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    private String invitationMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;

    private LocalDateTime expirationDate;

    @PrePersist
    private void setExpiration() {
        this.expirationDate = LocalDateTime.now().plusDays(7);
    }
}
