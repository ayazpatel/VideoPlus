package com.ayaz.authservice.dto;

/**
 * Response DTO for authentication operations
 * Contains the access token returned after successful login/registration
 */
public class AuthResponse {
    private String accessToken;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
