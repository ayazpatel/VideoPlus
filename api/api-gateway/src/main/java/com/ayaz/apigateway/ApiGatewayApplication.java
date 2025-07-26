package com.ayaz.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for API Gateway Service
 * This class starts the Spring Boot application and enables service discovery
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    /**
     * Main method - entry point for the application
     * Starts the Spring Boot application
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}