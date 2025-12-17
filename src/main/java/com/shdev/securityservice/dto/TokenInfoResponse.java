package com.shdev.securityservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

/**
 * Token info response DTO using Java Record with builder pattern.
 *
 * @author Shailesh Halor
 */
@Builder
public record TokenInfoResponse(
        @JsonProperty("iss") String issuer,
        @JsonProperty("aud") List<String> audience,
        @JsonProperty("exp") Long expiration,
        @JsonProperty("jti") String jwtId,
        @JsonProperty("iat") Long issuedAt,
        @JsonProperty("sub") String subject,
        @JsonProperty("client") String client,
        @JsonProperty("scope") List<String> scope,
        @JsonProperty("domain") String domain,
        @JsonProperty("v") String version,
        @JsonProperty("userRole") String userRole  // Roles in "ADMIN:USER" format
) {
}

