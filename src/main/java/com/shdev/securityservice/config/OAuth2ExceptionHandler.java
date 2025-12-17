package com.shdev.securityservice.config;

import com.shdev.securityservice.dto.ErrorResponse;
import com.shdev.securityservice.exception.InvalidClientException;
import com.shdev.securityservice.exception.InvalidRequestException;
import com.shdev.securityservice.exception.InvalidTokenException;
import com.shdev.securityservice.exception.UnsupportedGrantTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * OAuth2-specific exception handler that returns OAuth2-compliant error responses.
 *
 * @author Shailesh Halor
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OAuth2ExceptionHandler {

    /**
     * Handle invalid token exceptions.
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
        log.warn("Invalid token: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("invalid_token")
                .errorDescription(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Handle invalid client exceptions.
     */
    @ExceptionHandler(InvalidClientException.class)
    public ResponseEntity<ErrorResponse> handleInvalidClientException(InvalidClientException ex) {
        log.warn("Invalid client: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("invalid_client")
                .errorDescription(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Handle unsupported grant type exceptions.
     */
    @ExceptionHandler(UnsupportedGrantTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedGrantTypeException(UnsupportedGrantTypeException ex) {
        log.warn("Unsupported grant type: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("unsupported_grant_type")
                .errorDescription(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle invalid request exceptions.
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        log.warn("Invalid request: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("invalid_request")
                .errorDescription(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

