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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
