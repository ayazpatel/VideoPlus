package com.ayaz.authservice.exception;

import com.ayaz.authservice.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleApiErrorException(ApiErrorException ex) {
        ApiResponse<Object> response = new ApiResponse<>(ex.getStatus(), ex.getMessage(), null);
        return Mono.just(new ResponseEntity<>(response, ex.getStatus()));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleNoHandlerFoundException(ServerWebInputException ex) {
        ApiResponse<Object> response = new ApiResponse<>(HttpStatus.NOT_FOUND, "API endpoint not found", null);
        return Mono.just(new ResponseEntity<>(response, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleGlobalException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage(), null);
        return Mono.just(new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}