package com.ayaz.authservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ayaz.authservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret.access}")
    private String accessTokenSecret;
    @Value("${jwt.secret.refresh}")
    private String refreshTokenSecret;
    @Value("${jwt.expiration.access-token-ms}")
    private long accessTokenExpiration;
    @Value("${jwt.expiration.refresh-token-ms}")
    private long refreshTokenExpiration;

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getId())
                .withClaim("username", user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .sign(Algorithm.HMAC256(accessTokenSecret));
    }

    public String generateRefreshToken(User user) {
        return JWT.create()
                .withSubject(user.getId())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .sign(Algorithm.HMAC256(refreshTokenSecret));
    }

    /**
     * Validates the access token.
     * @return A Mono emitting the DecodedJWT on success, or an empty Mono if validation fails.
     */
    public Mono<DecodedJWT> validateAccessToken(String token) {
        return Mono.fromCallable(() -> {
            Algorithm algorithm = Algorithm.HMAC256(accessTokenSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        }).onErrorResume(JWTVerificationException.class, e -> Mono.empty()); // Return empty Mono on verification error
    }

    /**
     * Validates the refresh token.
     * @return A Mono emitting the DecodedJWT on success, or an empty Mono if validation fails.
     */
    public Mono<DecodedJWT> validateRefreshToken(String token) {
        return Mono.fromCallable(() -> {
            Algorithm algorithm = Algorithm.HMAC256(refreshTokenSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        }).onErrorResume(JWTVerificationException.class, e -> Mono.empty()); // Return empty Mono on verification error
    }
}