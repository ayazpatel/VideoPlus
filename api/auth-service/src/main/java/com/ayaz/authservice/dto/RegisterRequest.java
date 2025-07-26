package com.ayaz.authservice.dto;

/**
 * Request DTO for user registration
 * Contains all the information needed to register a new user
 */
public class RegisterRequest {
    private String fullName;
    private String username;
    private String email;
    private String password;

    // Default constructor - creates an empty RegisterRequest object
    public RegisterRequest() {
    }

    // Constructor with all fields - creates a RegisterRequest with all information
    public RegisterRequest(String fullName, String username, String email, String password) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter method - returns the user's full name
    public String getFullName() {
        return fullName;
    }

    // Setter method - sets the user's full name
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter method - returns the username
    public String getUsername() {
        return username;
    }

    // Setter method - sets the username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter method - returns the email address
    public String getEmail() {
        return email;
    }

    // Setter method - sets the email address
    public void setEmail(String email) {
        this.email = email;
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