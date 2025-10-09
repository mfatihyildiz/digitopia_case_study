package com.casestudy.organizationservice.controller;

import com.casestudy.organizationservice.entity.Organization;
import com.casestudy.organizationservice.service.OrganizationMemberService;
import com.casestudy.organizationservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;
    private final OrganizationMemberService memberService;

    @GetMapping
    public ResponseEntity<List<Organization>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Organization> create(@RequestBody Organization organization) {
        return ResponseEntity.ok(service.create(organization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orgId}/members")
    public ResponseEntity<Void> addMember(@PathVariable UUID orgId, @RequestParam UUID userId) {
        service.getById(orgId);
        memberService.addMember(orgId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orgId}/members")
    public ResponseEntity<List<UUID>> listMembers(@PathVariable UUID orgId) {
        service.getById(orgId); // 404 guard
        return ResponseEntity.ok(memberService.listUserIds(orgId));
    }

    @DeleteMapping("/{orgId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable UUID orgId, @PathVariable UUID userId) {
        service.getById(orgId);
        memberService.removeMember(orgId, userId);
        return ResponseEntity.noContent().build();
    }
}
