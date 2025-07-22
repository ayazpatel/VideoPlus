package com.ayaz.userservice.controller;

import com.ayaz.userservice.dto.UserDtos;
import com.ayaz.userservice.model.User;
import com.ayaz.userservice.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;
    @Value("${auth.service.internal.url}")
    private String authServiceUrl;
    @Value("${minio.endpoint}")
    private String minioEndpoint;
    @Value("${minio.bucket-name}")
    private String bucketName;

    private Mono<String> uploadFileToStorageService(MultipartFile file, String userId, String fileType) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        return webClientBuilder.build().post()
                .uri("http://file-storage-service/api/files/upload?fileType={fileType}", fileType)
                .header("X-User-ID", userId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseMap -> (String) responseMap.get("objectName"));
    }

    private void deleteFileFromStorageService(String objectName) {
        if (StringUtils.hasText(objectName)) {
            webClientBuilder.build()
                    .method(HttpMethod.DELETE)
                    .uri("http://file-storage-service/api/files/delete")
                    .body(BodyInserters.fromValue(Map.of("objectName", objectName)))
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe();
        }
    }

    private String constructPublicUrl(String objectName) {
        if (!StringUtils.hasText(objectName)) {
            return null;
        }
        return minioEndpoint + "/" + bucketName + "/" + objectName;
    }

    private UserDtos.UserProfileResponse toUserProfileResponse(User user) {
        UserDtos.UserProfileResponse response = new UserDtos.UserProfileResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setHistory(user.getHistory());
        response.setAvatarUrl(constructPublicUrl(user.getAvatar()));
        response.setCoverImageUrl(constructPublicUrl(user.getCoverImage()));
        return response;
    }

    private java.util.List<String> convertHistoryToStrings(java.util.List<org.bson.types.ObjectId> history) {
        if (history == null) {
            return java.util.Collections.emptyList();
        }
        return history.stream()
                .map(org.bson.types.ObjectId::toString)
                .collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<UserDtos.UserProfileResponse>> getCurrentUser(@RequestHeader("X-User-ID") String userId) {
        return userRepository.findById(userId)
                .map(this::toUserProfileResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping("/account")
    public Mono<ResponseEntity<User>> updateAccount(@RequestHeader("X-User-ID") String userId, @RequestBody UserDtos.AccountUpdateRequest request) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    user.setFullName(request.getFullName());
                    user.setEmail(request.getEmail());
                    return userRepository.save(user);
                })
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/change-password")
    public Mono<ResponseEntity<String>> changePassword(@RequestHeader("X-User-ID") String userId, @RequestBody UserDtos.PasswordChangeRequest request) {
        UserDtos.InternalPasswordChangeRequest internalRequest = new UserDtos.InternalPasswordChangeRequest();
        internalRequest.setUserId(userId);
        internalRequest.setOldPassword(request.getOldPassword());
        internalRequest.setNewPassword(request.getNewPassword());

        return webClientBuilder.build().post()
                .uri(authServiceUrl + "/change-password")
                .bodyValue(internalRequest)
                .retrieve()
                .toBodilessEntity()
                .map(response -> ResponseEntity.status(response.getStatusCode()).body("Password changed successfully"))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password: " + e.getMessage())));
    }

    @PatchMapping("/avatar")
    public Mono<ResponseEntity<UserDtos.UserProfileResponse>> updateAvatar(@RequestHeader("X-User-ID") String userId, @RequestParam("avatar") MultipartFile file) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    String oldAvatarKey = user.getAvatar();
                    return uploadFileToStorageService(file, userId, "avatar")
                            .flatMap(newAvatarKey -> {
                                user.setAvatar(newAvatarKey);
                                deleteFileFromStorageService(oldAvatarKey);
                                return userRepository.save(user);
                            });
                })
                .map(this::toUserProfileResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PatchMapping("/cover-image")
    public Mono<ResponseEntity<UserDtos.UserProfileResponse>> updateCoverImage(@RequestHeader("X-User-ID") String userId, @RequestParam("coverImage") MultipartFile file) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    String oldCoverImageKey = user.getCoverImage();
                    return uploadFileToStorageService(file, userId, "cover-image")
                            .flatMap(newCoverImageKey -> {
                                user.setCoverImage(newCoverImageKey);
                                deleteFileFromStorageService(oldCoverImageKey);
                                return userRepository.save(user);
                            });
                })
                .map(this::toUserProfileResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

//    @GetMapping("/history")
//    public Mono<ResponseEntity<Object>> getWatchHistory(@RequestHeader("X-User-ID") String userId) {
//        return userRepository.findById(userId)
//                .map(user -> {
//                    List<org.bson.types.ObjectId> historyList = user.getHistory();
//
//                    // Check if history is null or empty
//                    if (historyList == null || historyList.isEmpty()) {
//                        // Return a Map containing the custom message
//                        Map<String, String> responseMessage = Map.of("message", "no history of user available");
//                        return ResponseEntity.ok((Object) responseMessage);
//                    } else {
//                        // Otherwise, convert the history and return the list
//                        List<String> historyStrings = convertHistoryToStrings(historyList);
//                        return ResponseEntity.ok((Object) historyStrings);
//                    }
//                })
//                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
//    }

    @GetMapping("/history")
    public Mono<ResponseEntity<List<String>>> getWatchHistory(@RequestHeader("X-User-ID") String userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    // Safely handle a null history list by converting it to an empty list
                    List<org.bson.types.ObjectId> history = user.getHistory();
                    return history != null ? history : java.util.Collections.<org.bson.types.ObjectId>emptyList();
                })
                .map(this::convertHistoryToStrings) // Convert ObjectId to String
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}