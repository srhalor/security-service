package com.shdev.securityservice.service;

import com.shdev.securityservice.config.OAuth2ClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for validating OAuth2 client credentials.
 *
 * @author Shailesh Halor
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientCredentialsService {

    private final OAuth2ClientProperties clientProperties;
    private final PasswordEncoder passwordEncoder;

    /**
     * Validate client credentials and return client configuration if valid.
     *
     * @param clientId     the client identifier
     * @param clientSecret the client secret
     * @param domain       the identity domain
     * @return Optional containing client config if valid, empty otherwise
     */
    public Optional<OAuth2ClientProperties.ClientConfig> validateCredentials(
            String clientId, String clientSecret, String domain) {

        log.debug("Validating credentials for client: {}, domain: {}", clientId, domain);

        return clientProperties.getClients().stream()
                .filter(client -> client.getClientId().equals(clientId))
                .filter(client -> client.getIdentityDomain().equals(domain))
                .filter(client -> passwordEncoder.matches(clientSecret, client.getClientSecret()))
                .findFirst();
    }

    /**
     * Check if a scope is allowed for a client.
     *
     * @param clientConfig the client configuration
     * @param scope        the requested scope
     * @return true if scope is allowed, false otherwise
     */
    public boolean isScopeAllowed(OAuth2ClientProperties.ClientConfig clientConfig, String scope) {
        return clientConfig.getAllowedScopes().contains(scope);
    }
}

