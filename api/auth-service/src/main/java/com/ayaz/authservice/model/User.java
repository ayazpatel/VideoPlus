package com.ayaz.authservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User entity class for storing user information in MongoDB
 * This class represents a user in the authentication system
 */
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String fullName;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String refreshToken;

    // Default constructor - creates an empty User object
    public User() {
    }

    // Constructor with all fields - creates a User with all information
    public User(String id, String fullName, String username, String email, String password, String refreshToken) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
    }

    // Getter method - returns the user's unique ID
    public String getId() {
        return id;
    }

    // Setter method - sets the user's unique ID
    public void setId(String id) {
        this.id = id;
    }

    // Getter method - returns the user's full name
    public String getFullName() {
        return fullName;
    }

    // Setter method - sets the user's full name
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter method - returns the user's username
    public String getUsername() {
        return username;
    }

    // Setter method - sets the user's username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter method - returns the user's email address
    public String getEmail() {
        return email;
    }

    // Setter method - sets the user's email address
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter method - returns the user's password
    public String getPassword() {
        return password;
    }

    // Setter method - sets the user's password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter method - returns the user's refresh token
    public String getRefreshToken() {
        return refreshToken;
    }

    // Setter method - sets the user's refresh token
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}