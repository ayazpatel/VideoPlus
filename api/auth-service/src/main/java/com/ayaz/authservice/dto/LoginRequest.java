package com.ayaz.authservice.dto;

/**
 * Request DTO for user login
 * Contains username and password for authentication
 */
public class LoginRequest {
    private String username;
    private String password;

    // Default constructor - creates an empty LoginRequest object
    public LoginRequest() {
    }

    // Constructor with all fields - creates a LoginRequest with username and password
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter method - returns the username
    public String getUsername() {
        return username;
    }

    // Setter method - sets the username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter method - returns the password
    public String getPassword() {
        return password;
    }

    // Setter method - sets the password
    public void setPassword(String password) {
        this.password = password;
    }
}
