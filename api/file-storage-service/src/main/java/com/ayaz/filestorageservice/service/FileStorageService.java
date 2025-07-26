package com.ayaz.filestorageservice.service;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * Service class for file storage operations using MinIO
 * Handles file upload and delete operations
 */
@Service
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    // Constructor - initializes the service with MinioClient dependency
    public FileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Uploads a file to MinIO storage
     * Creates a unique object name and stores the file
     * Returns the object name/key of the uploaded file
     */
    public String uploadFile(MultipartFile file, String userId, String fileType) throws Exception {
        // Generate unique object name: userId/fileType/UUID-originalFilename
        String objectName = String.format("%s/%s/%s-%s", 
            userId, 
            fileType, 
            UUID.randomUUID().toString(), 
            file.getOriginalFilename()
        );

        // Check if bucket exists, create if not
        boolean found = minioClient.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()
        );
        
        if (!found) {
            // Create bucket if it doesn't exist
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
        }

        // Get input stream from the multipart file
        InputStream inputStream = file.getInputStream();
        
        // Upload file to MinIO
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, file.getSize(), -1) // Use actual file size
                .contentType(file.getContentType())
                .build()
        );

        // Close the input stream
        inputStream.close();

        return objectName; // Return the generated object name
    }

    /**
     * Deletes a file from MinIO storage
     * Removes the file using the provided object name/key
     */
    public void deleteFile(String objectName) throws Exception {
        // Delete file from MinIO
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build()
        );
    }
}