package com.shdev.securityservice.exception;

/**
 * Exception thrown when request validation fails.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}