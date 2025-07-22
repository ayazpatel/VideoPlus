//package com.ayaz.apigateway.filter;
//
//import com.ayaz.apigateway.util.JwtUtil;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * A custom GatewayFilter for authentication.
// * This filter intercepts requests, validates the JWT, and adds the user ID
// * to the request headers for downstream services. It works in a reactive, non-blocking way.
// */
//@Component
//public class AuthenticationFilter implements GatewayFilter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    // List of public endpoints that do not require authentication.
//    private final List<String> publicEndpoints = List.of(
//            "/api/auth/register",
//            "/api/auth/login",
//            "/api/auth/refresh-token"
//    );
//
//    /**
//     * The core logic of the filter.
//     * @param exchange The current server web exchange.
//     * @param chain The gateway filter chain.
//     * @return A Mono<Void> to indicate when processing is complete.
//     */
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getURI().getPath();
//
//        // If the endpoint is public, skip authentication and pass the request down the chain.
//        if (publicEndpoints.stream().anyMatch(path::startsWith)) {
//            return chain.filter(exchange);
//        }
//
//        // Check for the Authorization header.
//        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//            return onError(exchange, HttpStatus.UNAUTHORIZED);
//        }
//
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return onError(exchange, HttpStatus.UNAUTHORIZED);
//        }
//
//        // Extract the token from the header.
//        String token = authHeader.substring(7);
//
//        try {
//            // Validate the token and extract the user ID (subject).
//            DecodedJWT decodedJWT = jwtUtil.validateToken(token);
//            String userId = decodedJWT.getSubject();
//
//            // Add the user ID to the request headers for downstream services.
//            ServerHttpRequest modifiedRequest = request.mutate()
//                    .header("X-User-ID", userId)
//                    .build();
//
//            // Continue the filter chain with the modified request.
//            return chain.filter(exchange.mutate().request(modifiedRequest).build());
//
//        } catch (Exception e) {
//            // If token validation fails, return an unauthorized error.
//            return onError(exchange, HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * Helper method to build and return an error response.
//     * @param exchange The current server web exchange.
//     * @param status The HTTP status to return.
//     * @return A Mono<Void> to indicate the response is complete.
//     */
//    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
//        exchange.getResponse().setStatusCode(status);
//        return exchange.getResponse().setComplete();
//    }
//}

package com.ayaz.apigateway.filter;

import com.ayaz.apigateway.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtil jwtUtil;
    private final List<String> publicEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh-token"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        System.out.println("GATEWAY-FILTER: Incoming request for path: " + path);

        if (publicEndpoints.stream().anyMatch(path::startsWith)) {
            System.out.println("GATEWAY-FILTER: Path matched a public endpoint. Skipping authentication.");
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            System.out.println("GATEWAY-FILTER: Error - Missing Authorization header.");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("GATEWAY-FILTER: Found Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("GATEWAY-FILTER: Error - Header does not start with 'Bearer '.");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        try {
            System.out.println("GATEWAY-FILTER: Attempting to validate token: " + token);
            DecodedJWT decodedJWT = jwtUtil.validateToken(token);
            String userId = decodedJWT.getSubject();
            System.out.println("GATEWAY-FILTER: Token is VALID. Extracted userId: " + userId);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-ID", userId)
                    .build();

            System.out.println("GATEWAY-FILTER: Added 'X-User-ID' header. Forwarding to service.");
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } catch (Exception e) {
            System.out.println("GATEWAY-FILTER: Error - Token validation failed. Exception: " + e.getMessage());
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}