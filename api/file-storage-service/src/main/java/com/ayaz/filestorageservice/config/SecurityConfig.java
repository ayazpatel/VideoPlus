package com.ayaz.filestorageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // Secure endpoints by requiring a specific header added by the gateway
                        .pathMatchers("/api/files/**").hasAuthority("SCOPE_X-User-ID")
                        .anyExchange().denyAll()
                )
                // Use a custom filter to extract the header and set authentication
                .addFilterAt(new HeaderAuthenticationFilter(), org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}