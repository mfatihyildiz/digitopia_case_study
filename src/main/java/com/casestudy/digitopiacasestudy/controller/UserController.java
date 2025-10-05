package com.casestudy.digitopiacasestudy.controller;

import com.casestudy.digitopiacasestudy.entity.Organization;
import com.casestudy.digitopiacasestudy.entity.User;
import com.casestudy.digitopiacasestudy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search/email")
    public ResponseEntity<User> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/organizations")
    public ResponseEntity<List<Organization>> getUserOrganizations(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new ArrayList<>(user.getOrganizations()));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<User>> searchByNormalizedName(@RequestParam String name) {
        return ResponseEntity.ok(userService.searchByNormalizedName(name));
    }
}
