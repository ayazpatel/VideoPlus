package com.ayaz.authservice.controller;

import com.ayaz.authservice.dto.AuthResponse;
import com.ayaz.authservice.dto.LoginRequest;
import com.ayaz.authservice.dto.RegisterRequest;
import com.ayaz.authservice.exception.ApiErrorException;
import com.ayaz.authservice.model.User;
import com.ayaz.authservice.repository.UserRepository;
import com.ayaz.authservice.service.JwtService;
import com.ayaz.authservice.service.UserService;
import com.ayaz.authservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager authenticationManager;

    @PostMapping("/register")
    public Mono<ResponseEntity<ApiResponse<AuthResponse>>> registerUser(@RequestBody RegisterRequest request) {
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            return Mono.error(new ApiErrorException(HttpStatus.BAD_REQUEST, "Full name is required"));
        }

        return userRepository.existsByUsername(request.getUsername())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ApiErrorException(HttpStatus.CONFLICT, "Username is already taken"));
                    }
                    return userRepository.existsByEmail(request.getEmail());
                })
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ApiErrorException(HttpStatus.CONFLICT, "Email is already in use"));
                    }
                    User newUser = new User();
                    newUser.setFullName(request.getFullName());
                    newUser.setUsername(request.getUsername());
                    newUser.setEmail(request.getEmail());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    return userService.save(newUser);
                })
                .flatMap(savedUser -> {
                    String accessToken = jwtService.generateAccessToken(savedUser);
                    String refreshToken = jwtService.generateRefreshToken(savedUser);
                    savedUser.setRefreshToken(refreshToken);
                    return userService.save(savedUser).map(user -> {
                        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                                .httpOnly(true).secure(true).path("/").maxAge(7 * 24 * 60 * 60).build();

                        AuthResponse authResponse = new AuthResponse(accessToken);
                        ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.CREATED, "User registered successfully", authResponse);
                        return ResponseEntity.status(HttpStatus.CREATED)
                                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                .body(response);
                    });
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ApiResponse<AuthResponse>>> loginUser(@RequestBody LoginRequest request) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                )
                .map(Authentication::getPrincipal)
                .cast(User.class)
                .flatMap(user -> {
                    String accessToken = jwtService.generateAccessToken(user);
                    String refreshToken = jwtService.generateRefreshToken(user);
                    user.setRefreshToken(refreshToken);
                    return userService.save(user).map(savedUser -> {
                        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                                .httpOnly(true).secure(true).path("/").maxAge(7 * 24 * 60 * 60).build();
                        AuthResponse authResponse = new AuthResponse(accessToken);
                        ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.OK, "Login successful", authResponse);
                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                .body(response);
                    });
                });
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<ApiResponse<Object>>> logoutUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setRefreshToken(null);
        return userService.save(user).map(savedUser -> {
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true).secure(true).path("/").maxAge(0).build();
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK, "Logout successful", null);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(response);
        });
    }

    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<ApiResponse<AuthResponse>>> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return Mono.error(new ApiErrorException(HttpStatus.UNAUTHORIZED, "Refresh token is missing"));
        }

        return jwtService.validateRefreshToken(refreshToken)
                .switchIfEmpty(Mono.error(new ApiErrorException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")))
                .flatMap(decodedJWT -> {
                    String userId = decodedJWT.getSubject();
                    return userRepository.findById(userId)
                            .switchIfEmpty(Mono.error(new ApiErrorException(HttpStatus.UNAUTHORIZED, "User not found")));
                })
                .flatMap(user -> {
                    if (!refreshToken.equals(user.getRefreshToken())) {
                        return Mono.error(new ApiErrorException(HttpStatus.UNAUTHORIZED, "Refresh token is not valid"));
                    }
                    // Generate new tokens
                    String newAccessToken = jwtService.generateAccessToken(user);
                    String newRefreshToken = jwtService.generateRefreshToken(user);
                    user.setRefreshToken(newRefreshToken);

                    return userService.save(user).map(savedUser -> {
                        // Set the new refresh token in the cookie
                        ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                                .httpOnly(true).secure(true).path("/").maxAge(7 * 24 * 60 * 60).build();

                        AuthResponse authResponse = new AuthResponse(newAccessToken);
                        ApiResponse<AuthResponse> response = new ApiResponse<>(HttpStatus.OK, "Access token refreshed successfully", authResponse);
                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
                                .body(response);
                    });
                });
    }
}