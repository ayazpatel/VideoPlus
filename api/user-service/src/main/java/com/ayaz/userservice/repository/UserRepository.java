package com.ayaz.userservice.repository;

import com.ayaz.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for User entity in user service
 * Provides database operations for user profile management
 */
public interface UserRepository extends MongoRepository<User, String> {
    
    // Method to find a user by username - returns User object or null if not found
    User findByUsername(String username);
    
    // Method to find a user by email - returns User object or null if not found
    User findByEmail(String email);
    
    // Method to check if a user exists with given username - returns true if exists, false otherwise
    boolean existsByUsername(String username);
    
    // Method to check if a user exists with given email - returns true if exists, false otherwise
    boolean existsByEmail(String email);
}