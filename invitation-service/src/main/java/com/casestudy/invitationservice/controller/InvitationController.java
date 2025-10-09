package com.casestudy.invitationservice.controller;

import com.casestudy.invitationservice.entity.Invitation;
import com.casestudy.invitationservice.enums.InvitationStatus;
import com.casestudy.invitationservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService service;

    @GetMapping
    public ResponseEntity<List<Invitation>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invitation> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invitation>> getByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<Invitation>> getByOrganizationId(@PathVariable UUID orgId) {
        return ResponseEntity.ok(service.getByOrganizationId(orgId));
    }

    @PostMapping
    public ResponseEntity<Invitation> create(@RequestBody Invitation invitation) {
        Invitation created = service.create(invitation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Invitation> updateStatus(
            @PathVariable UUID id,
            @RequestParam InvitationStatus status
    ) {
        Invitation updated = service.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/expire")
    public ResponseEntity<Void> expireOldInvitations() {
        service.expireOld();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
