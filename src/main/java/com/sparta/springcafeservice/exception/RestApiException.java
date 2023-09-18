package com.sparta.springcafeservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiException extends RuntimeException {
    private final String message;
    private final int statusCode;

    public RestApiException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}

//GlobalExceptionHandler