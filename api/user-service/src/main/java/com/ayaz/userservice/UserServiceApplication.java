package com.ayaz.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for User Service
 * This class starts the Spring Boot application and enables service discovery
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    
    /**
     * Main method - entry point for the application
     * Starts the Spring Boot application
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}