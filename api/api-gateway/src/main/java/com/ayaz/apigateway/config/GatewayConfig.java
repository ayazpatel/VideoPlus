package com.ayaz.apigateway.config;

import com.ayaz.apigateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the Spring Cloud Gateway.
 * This class defines the routes to the downstream microservices.
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth service route - no authentication filter needed for login/register
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("http://localhost:9001"))

                // User service route - with authentication filter
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://localhost:9002"))

                // File storage service route - with authentication filter
                .route("file-storage-service", r -> r.path("/api/files/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://localhost:9003"))
                .build();
    }

    /**
     * Defines the routes for the API Gateway.
     * Each route is mapped to a load-balanced service URI (lb://) and a path pattern.
     * The AuthenticationFilter is applied to all routes to protect the endpoints.
     *
     * @param builder The RouteLocatorBuilder to build the routes.
     * @return The configured RouteLocator.
     */
//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("auth-service", r -> r.path("/api/auth/**")
////                        .filters(f -> f.filter(authenticationFilter))
//                        .uri("lb://auth-service"))
//                .route("user-service", r -> r.path("/api/users/**")
////                        .filters(f -> f.filter(authenticationFilter))
//                        .uri("lb://user-service"))
//                .route("file-storage-service", r -> r.path("/api/files/**")
////                        .filters(f -> f.filter(authenticationFilter))
//                        .uri("lb://file-storage-service"))
//                .build();
//    }
}
