package com.ayaz.userservice.controller;

import com.ayaz.userservice.model.User;
import com.ayaz.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for user management operations
 * Handles user profile operations like get, update, delete
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets all users from the database
     * Returns a list of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Gets a specific user by their ID
     * Returns the user if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets a user by their username
     * Returns the user if found, 404 if not found
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new user
     * Returns the created user with 201 status
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /**
     * Updates an existing user
     * Returns the updated user if successful, 404 if user not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = existingUser.get();
        
        // Update fields if they are provided
        if (updatedUser.getFullName() != null) {
            user.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getAvatar() != null) {
            user.setAvatar(updatedUser.getAvatar());
        }
        if (updatedUser.getCoverImage() != null) {
            user.setCoverImage(updatedUser.getCoverImage());
        }
        
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Deletes a user by their ID
     * Returns 204 No Content if successful, 404 if user not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}