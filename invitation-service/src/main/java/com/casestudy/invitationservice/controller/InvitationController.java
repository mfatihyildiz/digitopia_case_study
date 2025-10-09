package com.casestudy.invitationservice.controller;

import com.casestudy.invitationservice.entity.Invitation;
import com.casestudy.invitationservice.enums.InvitationStatus;
import com.casestudy.invitationservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Invitation>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invitation> byId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Invitation> create(@RequestBody Invitation inv) {
        return ResponseEntity.ok(service.create(inv));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Invitation> updateStatus(@PathVariable UUID id, @RequestParam InvitationStatus status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @PostMapping("/expire")
    public ResponseEntity<Void> expire() {
        service.expireOld();
        return ResponseEntity.noContent().build();
    }
}
