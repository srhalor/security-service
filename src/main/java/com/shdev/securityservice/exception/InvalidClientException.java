package com.shdev.securityservice.exception;

/**
 * Exception thrown when client authentication fails.
 */
public class InvalidClientException extends RuntimeException {
    public InvalidClientException(String message) {
        super(message);
    }
    public InvalidClientException(String message, Throwable cause) {
        super(message, cause);
    }
}