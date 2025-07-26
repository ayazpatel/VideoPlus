package com.ayaz.apigateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple API Gateway Controller
 * Routes requests to appropriate microservices
 */
@RestController
public class GatewayController {

    private final RestTemplate restTemplate;

    // Service URLs - configured for local development
    private static final String AUTH_SERVICE_URL = "http://localhost:9001";
    private static final String USER_SERVICE_URL = "http://localhost:9002";  
    private static final String FILE_SERVICE_URL = "http://localhost:9003";

    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Routes authentication requests to auth service
     * Handles login, register, logout operations
     */
    @RequestMapping(value = "/api/auth/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> routeToAuthService(HttpServletRequest request, @RequestBody(required = false) String body) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String targetUrl = AUTH_SERVICE_URL + path;
        
        try {
            // Forward request to auth service
            ResponseEntity<String> response;
            switch (method) {
                case "POST":
                    response = restTemplate.postForEntity(targetUrl, body, String.class);
                    break;
                case "PUT":
                    restTemplate.put(targetUrl, body);
                    response = ResponseEntity.ok().build();
                    break;
                case "DELETE":
                    restTemplate.delete(targetUrl);
                    response = ResponseEntity.ok().build();
                    break;
                default:
                    response = restTemplate.getForEntity(targetUrl, String.class);
                    break;
            }
            return response;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error routing to auth service: " + e.getMessage());
        }
    }

    /**
     * Routes user management requests to user service
     * Handles user profile operations
     */
    @RequestMapping(value = "/api/users/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> routeToUserService(HttpServletRequest request, @RequestBody(required = false) String body) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String targetUrl = USER_SERVICE_URL + path;
        
        try {
            // Forward request to user service
            ResponseEntity<String> response;
            switch (method) {
                case "POST":
                    response = restTemplate.postForEntity(targetUrl, body, String.class);
                    break;
                case "PUT":
                    restTemplate.put(targetUrl, body);



                    response = ResponseEntity.ok().build();
                    break;
                case "DELETE":
                    restTemplate.delete(targetUrl);
                    response = ResponseEntity.ok().build();
                    break;
                default:
                    response = restTemplate.getForEntity(targetUrl, String.class);
                    break;
            }
            return response;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error routing to user service: " + e.getMessage());
        }
    }

    /**
     * Routes file storage requests to file storage service
     * Handles file upload and delete operations
     */
    @RequestMapping(value = "/api/files/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> routeToFileService(HttpServletRequest request, @RequestBody(required = false) String body) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String targetUrl = FILE_SERVICE_URL + path;
        
        try {
            // Forward request to file storage service
            ResponseEntity<String> response;
            switch (method) {
                case "POST":
                    response = restTemplate.postForEntity(targetUrl, body, String.class);
                    break;
                case "PUT":
                    restTemplate.put(targetUrl, body);
                    response = ResponseEntity.ok().build();
                    break;
                case "DELETE":
                    restTemplate.delete(targetUrl);
                    response = ResponseEntity.ok().build();
                    break;
                default:
                    response = restTemplate.getForEntity(targetUrl, String.class);
                    break;
            }
            return response;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error routing to file service: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint
     * Returns the status of the API Gateway
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "API Gateway");
        return ResponseEntity.ok(response);
    }
}
