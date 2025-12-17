package com.shdev.securityservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class for handling Authorization headers.
 *
 * @author Shailesh Halor
 */
@Slf4j
public final class AuthorizationUtil {

    private AuthorizationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Extract client ID from Basic Auth header.
     *
     * @param authorization Authorization header value
     * @return client ID or null if invalid
     */
    public static String extractClientId(String authorization) {
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Basic ")) {
            return null;
        }

        try {
            String base64Credentials = authorization.substring("Basic ".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);

            String[] values = credentials.split(":", 2);
            if (values.length == 2) {
                return values[0];
            }
        } catch (Exception e) {
            log.warn("Failed to decode Basic Auth header", e);
        }

        return null;
    }

    /**
     * Extract client credentials (both ID and secret) from Basic Auth header.
     *
     * @param authorization Authorization header value
     * @return array containing [clientId, clientSecret] or null if invalid
     */
    public static String[] extractClientCredentials(String authorization) {
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Basic ")) {
            return null;
        }

        try {
            String base64Credentials = authorization.substring("Basic ".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);

            String[] values = credentials.split(":", 2);
            if (values.length == 2) {
                return values;
            }
        } catch (Exception e) {
            log.warn("Failed to decode Basic Auth header", e);
        }

        return null;
    }

    /**
     * Create Basic Auth header value from client credentials.
     *
     * @param clientId     client identifier
     * @param clientSecret client secret
     * @return Basic Auth header value
     */
    public static String createBasicAuthHeader(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(
                credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encodedCredentials;
    }
}

