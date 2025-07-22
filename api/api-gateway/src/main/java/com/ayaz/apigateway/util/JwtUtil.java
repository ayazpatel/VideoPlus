package com.ayaz.apigateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JWTs.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret.access}")
    private String accessTokenSecret;

    /**
     * Validates the given JWT. It uses the HMAC256 algorithm and the configured
     * secret key to verify the token's signature.
     *
     * @param token The JWT to validate.
     * @return The decoded JWT if valid.
     * @throws JWTVerificationException if the token is invalid (e.g., bad signature, expired).
     */
    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(accessTokenSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
