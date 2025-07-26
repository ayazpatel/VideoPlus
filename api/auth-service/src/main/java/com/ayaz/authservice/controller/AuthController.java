package com.ayaz.authservice.controller;

import com.ayaz.authservice.dto.AuthResponse;
import com.ayaz.authservice.dto.LoginRequest;
import com.ayaz.authservice.dto.RegisterRequest;
import com.ayaz.authservice.model.User;
import com.ayaz.authservice.repository.UserRepository;
import com.ayaz.authservice.service.UserService;
import com.ayaz.authservice.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication operations
 * Handles user registration, login, and logout requests
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    // Constructor - initializes the controller with required services
    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user in the system
     * Validates user input and creates a new user account
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@RequestBody RegisterRequest request) {
        try {
            // Check if full name is provided
            if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
                ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Full name is required", null);
                return ResponseEntity.badRequest().body(response);
            }

            // Check if username already exists
            if (userRepository.existsByUsername(request.getUsername())) {
                ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.CONFLICT, "Username is already taken", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.CONFLICT, "Email is already in use", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Create new user object
            User newUser = new User();
            newUser.setFullName(request.getFullName());
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword()); // In real app, this should be encoded

            // Save user to database
            User savedUser = userService.save(newUser);

            // Create authentication response (simplified - no actual JWT token generation)
            AuthResponse authResponse = new AuthResponse("dummy_token_for_user_" + savedUser.getUsername());
            ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.CREATED, "User registered successfully", authResponse);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Registration failed: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Authenticates a user and logs them into the system
     * Validates credentials and returns access token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(@RequestBody LoginRequest request) {
        try {
            // Check if request parameters are provided
            if (request.getUsername() == null || request.getPassword() == null) {
                ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Username and password are required", null);
                return ResponseEntity.badRequest().body(response);
            }

            // Find user by username
            User user = userRepository.findByUsername(request.getUsername());
            
            if (user == null) {
                ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Invalid username or password", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Check password (in real app, this should use encoded password comparison)
            if (!user.getPassword().equals(request.getPassword())) {
                ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Invalid username or password", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Create authentication response (simplified - no actual JWT token generation)
            AuthResponse authResponse = new AuthResponse("dummy_token_for_user_" + user.getUsername());
            ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.OK, "Login successful", authResponse);
            
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Logs out a user from the system
     * Clears user session and tokens
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logoutUser() {
        try {
            // In a real application, you would invalidate the user's token here
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK, "Logout successful", null);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Logout failed: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Health check endpoint for the auth service
     * Returns the status of the authentication service
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK, "Auth service is running", "UP");
        return ResponseEntity.ok().body(response);
    }
}
