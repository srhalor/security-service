package com.shdev.securityservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * JWT token validator using RS256 algorithm.
 *
 * @author Shailesh Halor
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtProperties jwtProperties;
    private PublicKey publicKey;

    /**
     * Validate JWT token and return claims.
     *
     * @param token the JWT token string
     * @return map of claims
     * @throws TokenValidationException if token is invalid
     */
    public Map<String, Object> validateToken(String token) throws TokenValidationException {
        log.debug("Validating JWT token");

        if (publicKey == null) {
            publicKey = loadPublicKey();
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("JWT token validated successfully for subject: {}", claims.getSubject());
            return claims;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired", e);
            throw new TokenValidationException("Token has expired", e);
        } catch (SignatureException e) {
            log.warn("JWT token signature validation failed", e);
            throw new TokenValidationException("Invalid token signature", e);
        } catch (MalformedJwtException e) {
            log.warn("JWT token is malformed", e);
            throw new TokenValidationException("Malformed token", e);
        } catch (Exception e) {
            log.error("JWT token validation failed", e);
            throw new TokenValidationException("Token validation failed", e);
        }
    }

    /**
     * Load public key from file.
     *
     * @return PublicKey instance
     */
    private PublicKey loadPublicKey() {
        try {
            log.info("Loading public key from: {}", jwtProperties.getPublicKeyPath());
            String key = new String(Files.readAllBytes(Paths.get(jwtProperties.getPublicKeyPath())));

            // Remove PEM headers/footers and whitespace
            key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            log.info("Public key loaded successfully");
            return keyFactory.generatePublic(spec);
        } catch (IOException e) {
            log.error("Failed to read public key file: {}", jwtProperties.getPublicKeyPath(), e);
            throw new RuntimeException("Failed to load public key", e);
        } catch (Exception e) {
            log.error("Failed to parse public key", e);
            throw new RuntimeException("Failed to parse public key", e);
        }
    }

    /**
     * Exception thrown when token validation fails.
     */
    public static class TokenValidationException extends Exception {
        public TokenValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

