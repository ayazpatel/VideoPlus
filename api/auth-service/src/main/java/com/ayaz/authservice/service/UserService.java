package com.ayaz.authservice.service;

import com.ayaz.authservice.model.User;
import com.ayaz.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for user-related business logic
 * Handles user operations like saving, finding, and validating users
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor - initializes the service with UserRepository dependency
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by username
     * Returns the user if found, null if not found
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by ID
     * Returns the user if found, null if not found
     */
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Saves a user to the database
     * Returns the saved user with generated ID
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Checks if a user exists with the given username
     * Returns true if exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user exists with the given email
     * Returns true if exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}