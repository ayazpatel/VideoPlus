package com.ayaz.authservice.util;

import org.springframework.http.HttpStatus;

/**
 * Generic API response wrapper class
 * Provides consistent response structure for all API endpoints
 */
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private boolean success;

    // Constructor - creates an API response with status, message and data
    public ApiResponse(HttpStatus statusCode, String message, T data) {
        this.statusCode = statusCode.value();
        this.message = message;
        this.data = data;
        this.success = statusCode.is2xxSuccessful();
    }

    // Getter method - returns the HTTP status code
    public int getStatusCode() {
        return statusCode;
    }

    // Setter method - sets the HTTP status code
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    // Getter method - returns the response message
    public String getMessage() {
        return message;
    }

    // Setter method - sets the response message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter method - returns the response data
    public T getData() {
        return data;
    }

    // Setter method - sets the response data
    public void setData(T data) {
        this.data = data;
    }

    // Getter method - returns whether the operation was successful
    public boolean isSuccess() {
        return success;
    }

    // Setter method - sets the success flag
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
