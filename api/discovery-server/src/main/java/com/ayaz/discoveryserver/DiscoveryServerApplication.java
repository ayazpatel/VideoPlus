package com.ayaz.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for Discovery Server (Eureka Server)
 * This class starts the Spring Boot application with Eureka Server capabilities
 * Other microservices register themselves with this discovery server
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    
    /**
     * Main method - entry point for the application
     * Starts the Eureka Server for service discovery
     */
    public static void main(String[] args) {
        System.out.println("Discovery Server (Eureka) is starting up...");
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}