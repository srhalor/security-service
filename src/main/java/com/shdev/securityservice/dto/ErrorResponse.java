package com.shdev.securityservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Error response DTO using Java Record with builder pattern.
 *
 * @author Shailesh Halor
 */
@Builder
public record ErrorResponse(
        @JsonProperty("error") String error,
        @JsonProperty("error_description") String errorDescription
) {
}

