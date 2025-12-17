package com.shdev.securityservice.controller;

import com.shdev.securityservice.config.OAuth2ClientProperties;
import com.shdev.securityservice.dto.TokenInfoResponse;
import com.shdev.securityservice.dto.TokenResponse;
import com.shdev.securityservice.exception.InvalidClientException;
import com.shdev.securityservice.exception.InvalidRequestException;
import com.shdev.securityservice.exception.UnsupportedGrantTypeException;
import com.shdev.securityservice.service.ClientCredentialsService;
import com.shdev.securityservice.service.TokenService;
import com.shdev.securityservice.util.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for OAuth2 token operations.
 * Error handling is delegated to GlobalExceptionHandler.
 *
 * @author Shailesh Halor
 */
@Slf4j
@RestController
@RequestMapping("/oauth2/rest/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final ClientCredentialsService clientCredentialsService;

    /**
     * Generate JWT access token (OAuth2 token endpoint).
     *
     * @param identityDomainName identity domain name from header
     * @param authorization      Basic authentication header
     * @param grantType          OAuth2 grant type (must be CLIENT_CREDENTIALS)
     * @param scope              token scope
     * @return TokenResponse with access token
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public TokenResponse generateToken(
            @RequestHeader(value = "X-OAUTH-IDENTITY-DOMAIN-NAME", required = false) String identityDomainName,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(value = "grant_type") String grantType,
            @RequestParam(value = "scope") String scope) {

        log.info("Received token generation request for domain: {}, grant_type: {}", identityDomainName, grantType);

        // Validate grant type
        if (!"CLIENT_CREDENTIALS".equals(grantType)) {
            log.warn("Invalid grant type: {}", grantType);
            throw new UnsupportedGrantTypeException("Only CLIENT_CREDENTIALS grant type is supported");
        }

        // Validate domain
        if (!StringUtils.hasText(identityDomainName)) {
            log.warn("Missing X-OAUTH-IDENTITY-DOMAIN-NAME header");
            throw new InvalidRequestException("Missing X-OAUTH-IDENTITY-DOMAIN-NAME header");
        }

        // Validate and extract Basic Auth credentials
        String[] credentials = AuthorizationUtil.extractClientCredentials(authorization);
        if (credentials == null || credentials.length != 2) {
            log.warn("Missing or invalid Authorization header");
            throw new InvalidClientException("Invalid or missing client credentials");
        }

        String clientId = credentials[0];
        String clientSecret = credentials[1];

        log.info("Validating client credentials for client: [{}], clientSecret: [{}], identityDomainName: [{}]", clientId, clientSecret, identityDomainName);

        // Validate client credentials
        Optional<OAuth2ClientProperties.ClientConfig> clientConfig =
                clientCredentialsService.validateCredentials(clientId, clientSecret, identityDomainName);

        if (clientConfig.isEmpty()) {
            log.warn("Invalid client credentials for client: {}", clientId);
            throw new InvalidClientException("Invalid client credentials");
        }

        // Validate scope
        if (!clientCredentialsService.isScopeAllowed(clientConfig.get(), scope)) {
            log.warn("Scope not allowed for client {}: {}", clientId, scope);
            throw new InvalidRequestException("Scope '" + scope + "' is not allowed for this client");
        }

        // Get roles from client configuration (default to empty list if not configured)
        java.util.List<String> roles = clientConfig.get().getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = java.util.List.of("USER");  // Default role if none configured
            log.debug("No roles configured for client {}, using default: USER", clientId);
        }

        TokenResponse response = tokenService.generateToken(clientId, scope, identityDomainName, roles);
        log.info("Token generated successfully for client: {} with roles: {}", clientId, roles);
        return response;
    }

    /**
     * Validate JWT token and return payload (token info endpoint).
     *
     * @param accessToken        JWT token to validate
     * @param identityDomainName identity domain name from header
     * @return TokenInfoResponse with token payload
     */
    @GetMapping("/info")
    public TokenInfoResponse validateToken(
            @RequestParam(value = "access_token") String accessToken,
            @RequestHeader(value = "X-OAUTH-IDENTITY-DOMAIN-NAME", required = false) String identityDomainName) {

        log.info("Received token validation request for domain: {}", identityDomainName);

        if (!StringUtils.hasText(accessToken)) {
            log.warn("Missing access_token parameter");
            throw new InvalidRequestException("Missing access_token parameter");
        }

        TokenInfoResponse response = tokenService.validateToken(accessToken);
        log.info("Token validated successfully for subject: {}", response.subject());
        return response;
    }
}

