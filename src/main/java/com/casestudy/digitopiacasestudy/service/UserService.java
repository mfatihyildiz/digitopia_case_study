package com.casestudy.digitopiacasestudy.service;

import com.casestudy.digitopiacasestudy.entity.User;
import com.casestudy.digitopiacasestudy.enums.UserStatus;
import com.casestudy.digitopiacasestudy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        user.setStatus(UserStatus.PENDING);
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        User existing = getUserById(id);
        existing.setFullName(updatedUser.getFullName());
        existing.setStatus(updatedUser.getStatus());
        existing.setRole(updatedUser.getRole());
        return userRepository.save(existing);
    }

    public void deleteUser(UUID id) {
        User user = getUserById(id);
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }
}
