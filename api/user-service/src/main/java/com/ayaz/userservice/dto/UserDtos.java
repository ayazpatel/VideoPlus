package com.ayaz.userservice.dto;

import lombok.Data;
import java.util.List;

public class UserDtos {
    @Data
    public static class AccountUpdateRequest {
        private String fullName;
        private String email;
    }

    @Data
    public static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;
    }

    @Data
    public static class InternalPasswordChangeRequest {
        private String userId;
        private String oldPassword;
        private String newPassword;
    }

    // DTO for returning a user profile with full URLs
    @Data
    public static class UserProfileResponse {
        private String id;
        private String fullName;
        private String username;
        private String email;
        private String avatarUrl; // Full public URL
        private String coverImageUrl; // Full public URL
        private List<?> history;
    }
}