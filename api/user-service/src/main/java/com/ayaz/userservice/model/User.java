package com.ayaz.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * User entity class for storing user profile information in MongoDB
 * This class represents a user profile in the user management system
 */
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String avatar; // Stores the MinIO object key (e.g., user-123/avatar/image.jpg)
    private String coverImage; // Stores the MinIO object key
    private List<String> history; // Changed from ObjectId to String for simplicity

    // Default constructor - creates an empty User object
    public User() {
    }

    // Constructor with basic fields
    public User(String fullName, String username, String email) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
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

    // Getter method - returns the avatar image path
    public String getAvatar() {
        return avatar;
    }

    // Setter method - sets the avatar image path
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // Getter method - returns the cover image path
    public String getCoverImage() {
        return coverImage;
    }

    // Setter method - sets the cover image path
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    // Getter method - returns the user's history list
    public List<String> getHistory() {
        return history;
    }

    // Setter method - sets the user's history list
    public void setHistory(List<String> history) {
        this.history = history;
    }
}