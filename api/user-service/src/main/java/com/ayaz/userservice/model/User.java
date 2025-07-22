package com.ayaz.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String fullName;
    private String username;
    private String email;
    private String avatar; // Stores the MinIO object key (e.g., user-123/avatar/image.jpg)
    private String coverImage; // Stores the MinIO object key
    private List<ObjectId> history;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String refreshToken;
}