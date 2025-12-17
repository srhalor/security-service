package com.shdev.securityservice.service;

import com.shdev.securityservice.util.TypeConversionUtil;
import com.shdev.securityservice.jwt.JwtProperties;
import com.shdev.securityservice.jwt.JwtTokenGenerator;
import com.shdev.securityservice.jwt.JwtTokenValidator;
import com.shdev.securityservice.dto.TokenInfoResponse;
import com.shdev.securityservice.dto.TokenResponse;
import com.shdev.securityservice.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service for JWT token generation and validation.
 *
 * @author Shailesh Halor
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtProperties jwtProperties;

    /**
     * Generate JWT access token.
     *
     * @param clientId client identifier
     * @param scope    token scope
     * @param domain   identity domain name
     * @param roles    user roles for this client
     * @return TokenResponse containing access token
     */
    public TokenResponse generateToken(String clientId, String scope, String domain, java.util.List<String> roles) {
        log.info("Generating token for client: {}, domain: {}, roles: {}", clientId, domain, roles);

        String token = jwtTokenGenerator.generateToken(clientId, scope, domain, roles);

        return TokenResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpirationSeconds())
                .scope(scope)
                .build();
    }

    /**
     * Validate JWT token and return payload information.
     *
     * @param token JWT token string
     * @return TokenInfoResponse containing token claims
     * @throws InvalidTokenException if validation fails
     */
    public TokenInfoResponse validateToken(String token) {
        log.info("Validating token");

        try {
            Map<String, Object> claims = jwtTokenValidator.validateToken(token);

            // Extract roles and format as colon-separated string
            java.util.List<String> roles = TypeConversionUtil.toStringList(claims.get("roles"));
            String userRole = !roles.isEmpty()
                    ? String.join(":", roles)
                    : null;

            return TokenInfoResponse.builder()
                    .issuer(TypeConversionUtil.toString(claims.get("iss")))
                    .audience(TypeConversionUtil.toStringList(claims.get("aud")))
                    .expiration(TypeConversionUtil.toLong(claims.get("exp")))
                    .jwtId(TypeConversionUtil.toString(claims.get("jti")))
                    .issuedAt(TypeConversionUtil.toLong(claims.get("iat")))
                    .subject(TypeConversionUtil.toString(claims.get("sub")))
                    .client(TypeConversionUtil.toString(claims.get("client")))
                    .scope(TypeConversionUtil.toStringList(claims.get("scope")))
                    .domain(TypeConversionUtil.toString(claims.get("domain")))
                    .version(TypeConversionUtil.toString(claims.get("v")))
                    .userRole(userRole)
                    .build();
        } catch (JwtTokenValidator.TokenValidationException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            throw new InvalidTokenException(e.getMessage(), e);
        }
    }
}


