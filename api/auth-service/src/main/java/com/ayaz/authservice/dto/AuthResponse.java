package com.ayaz.authservice.dto;

/**
 * Response DTO for authentication operations
 * Contains the access token returned after successful login/registration
 */
public class AuthResponse {
    private String accessToken;

    // Default constructor - creates an empty AuthResponse object
    public AuthResponse() {
    }

    // Constructor with access token - creates an AuthResponse with token
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // Getter method - returns the access token
    public String getAccessToken() {
        return accessToken;
    }

    // Setter method - sets the access token
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
