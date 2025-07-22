package com.ayaz.authservice.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private boolean success;

    public ApiResponse(HttpStatus statusCode, String message, T data) {
        this.statusCode = statusCode.value();
        this.message = message;
        this.data = data;
        this.success = statusCode.is2xxSuccessful();
    }
}
