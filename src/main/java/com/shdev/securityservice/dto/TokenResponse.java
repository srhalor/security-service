package com.shdev.securityservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Token response DTO using Java Record with builder pattern.
 *
 * @author Shailesh Halor
 */
@Builder
public record TokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn,
        @JsonProperty("scope") String scope
) {
}

