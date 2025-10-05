package com.casestudy.digitopiacasestudy.controller;

import com.casestudy.digitopiacasestudy.entity.Organization;
import com.casestudy.digitopiacasestudy.entity.User;
import com.casestudy.digitopiacasestudy.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Organization>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(organizationService.searchOrganizations(name, year, size));
    }

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@Valid @RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.createOrganization(organization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable UUID id,
                                                           @RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.updateOrganization(id, organization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getOrganizationUsers(@PathVariable UUID id) {
        Organization organization = organizationService.getById(id);
        return ResponseEntity.ok(new ArrayList<>(organization.getUsers()));
    }

    @GetMapping("/search/registry")
    public ResponseEntity<Organization> getByRegistryNumber(@RequestParam String registryNumber) {
        return ResponseEntity.ok(organizationService.getByRegistryNumber(registryNumber));
    }
}
