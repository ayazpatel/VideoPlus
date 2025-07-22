package com.ayaz.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE); // Set to REACTIVE for WebFlux
        app.run(args);
    }

    @Bean
    @LoadBalanced // Enable client-side load balancing for WebClient
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}