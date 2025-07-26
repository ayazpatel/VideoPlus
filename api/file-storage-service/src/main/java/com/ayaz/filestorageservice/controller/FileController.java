package com.ayaz.filestorageservice.controller;

import com.ayaz.filestorageservice.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for file storage operations
 * Handles file upload and delete operations using MinIO
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    // Constructor - initializes the controller with FileStorageService dependency
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * Uploads a file to MinIO storage
     * Returns the object name/key of the uploaded file
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType,
            @RequestHeader(value = "X-User-ID", required = false) String userId
    ) {
        try {
            // Use a default user ID if not provided
            if (userId == null || userId.isEmpty()) {
                userId = "anonymous";
            }
            
            String objectName = fileStorageService.uploadFile(file, userId, fileType);
            
            Map<String, String> response = new HashMap<>();
            response.put("objectName", objectName);
            response.put("message", "File uploaded successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Deletes a file from MinIO storage
     * Returns success or error response
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteFile(@RequestBody Map<String, String> payload) {
        try {
            String objectName = payload.get("objectName");
            
            if (objectName == null || objectName.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Object name is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            fileStorageService.deleteFile(objectName);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "File deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}