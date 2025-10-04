package com.casestudy.digitopiacasestudy.controller;

import com.casestudy.digitopiacasestudy.entity.Invitation;
import com.casestudy.digitopiacasestudy.enums.InvitationStatus;
import com.casestudy.digitopiacasestudy.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @GetMapping
    public ResponseEntity<List<Invitation>> getAllInvitations() {
        return ResponseEntity.ok(invitationService.getAllInvitations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invitation> getInvitation(@PathVariable UUID id) {
        return ResponseEntity.ok(invitationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Invitation> createInvitation(@RequestBody Invitation invitation) {
        return ResponseEntity.ok(invitationService.createInvitation(invitation));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Invitation> updateStatus(@PathVariable UUID id, @RequestParam InvitationStatus status) {
        return ResponseEntity.ok(invitationService.updateStatus(id, status));
    }

    @PostMapping("/expire")
    public ResponseEntity<Void> expireOldInvitations() {
        invitationService.expireOldInvitations();
        return ResponseEntity.noContent().build();
    }
}
