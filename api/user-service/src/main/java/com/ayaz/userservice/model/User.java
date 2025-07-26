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

    // Default constructor
    public User() {
    }

    public User(String fullName, String username, String email) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }
}