package com.sparta.springcafeservice.exception;

public class RestApiException extends RuntimeException {
    private final String message;
    private final int statusCode;

    public RestApiException(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}