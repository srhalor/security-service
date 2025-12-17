package com.shdev.securityservice.exception;

/**
 * Exception thrown when unsupported grant type is requested.
 */
public class UnsupportedGrantTypeException extends RuntimeException {
    public UnsupportedGrantTypeException(String message) {
        super(message);
    }
    public UnsupportedGrantTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}