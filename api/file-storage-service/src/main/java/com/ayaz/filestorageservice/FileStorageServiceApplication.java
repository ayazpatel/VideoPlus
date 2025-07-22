package com.ayaz.filestorageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FileStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FileStorageServiceApplication.class);
        // Set the application type to REACTIVE for WebFlux
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.run(args);
    }
}