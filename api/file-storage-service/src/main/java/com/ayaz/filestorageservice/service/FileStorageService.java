package com.ayaz.filestorageservice.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public Mono<String> uploadFile(FilePart filePart, String userId, String fileType) {
        String objectName = String.format("%s/%s/%s-%s", userId, fileType, UUID.randomUUID(), filePart.filename());

        return Mono.fromCallable(() -> {
            // Check if bucket exists, create if not. This is a blocking call.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // A PipedInputStream is used to bridge the reactive data buffer to the blocking InputStream
            PipedInputStream pipedInputStream = new PipedInputStream();
            PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);

            // The data from the reactive FilePart is written to the PipedOutputStream
            filePart.content().doOnComplete(() -> {
                try {
                    pipedOutputStream.close();
                } catch (IOException ignored) {}
            }).subscribe(dataBuffer -> {
                try {
                    pipedOutputStream.write(dataBuffer.asByteBuffer().array());
                } catch (IOException ignored) {}
            });

            // The blocking MinIO client reads from the PipedInputStream
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(pipedInputStream, -1, 10485760) // Use -1 for size to let MinIO handle it
                            .contentType(filePart.headers().getContentType().toString())
                            .build()
            );

            return objectName; // Return the generated object name, not the full URL
        }).subscribeOn(Schedulers.boundedElastic()); // Execute the blocking code on a separate thread pool
    }

    public Mono<Void> deleteFile(String objectName) {
        return Mono.<Void>fromRunnable(() -> {
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build()
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete file from MinIO", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}