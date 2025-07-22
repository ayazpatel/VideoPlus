package com.ayaz.filestorageservice.controller;

import com.ayaz.filestorageservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public Mono<ResponseEntity<Map<String, String>>> uploadFile(
            @RequestPart("file") Mono<FilePart> filePartMono,
            @RequestParam("fileType") String fileType,
            Principal principal
    ) {
        String userId = principal.getName();
        return filePartMono
                .flatMap(filePart -> fileStorageService.uploadFile(filePart, userId, fileType))
                .map(objectName -> ResponseEntity.ok(Map.of("objectName", objectName)))
                .onErrorResume(e -> {
                    // Create a Map for the error response to ensure a consistent JSON object structure
                    Map<String, String> errorResponse = Map.of("error", "Failed to upload file: " + e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
                });
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Void>> deleteFile(@RequestBody Map<String, String> payload) {
        String objectName = payload.get("objectName");
        return fileStorageService.deleteFile(objectName)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
    }
}