package com.ayaz.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for REST client
 * Provides RestTemplate bean for making HTTP requests to other services
 */
@Configuration
public class RestConfig {

    /**
     * Creates RestTemplate bean for HTTP requests
     * Used by the gateway to communicate with other microservices
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
