package com.shdev.securityservice.dto;

/**
 * Token request DTO using Java Record.
 *
 * @author Shailesh Halor
 */
public record TokenRequest(
        String grantType,
        String scope
) {
}

