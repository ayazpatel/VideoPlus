package com.ayaz.authservice.repository;

import com.ayaz.authservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for User entity
 * Provides database operations for user management
 */
public interface UserRepository extends MongoRepository<User, String> {
    
    // Method to find a user by username - returns User object or null if not found
    User findByUsername(String username);
    
    // Method to check if a user exists with given username - returns true if exists, false otherwise
    boolean existsByUsername(String username);
    
    // Method to check if a user exists with given email - returns true if exists, false otherwise
    boolean existsByEmail(String email);
}