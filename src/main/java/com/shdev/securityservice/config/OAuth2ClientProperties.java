package com.shdev.securityservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for OAuth2 clients.
 *
 * @author Shailesh Halor
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2ClientProperties {

    private List<ClientConfig> clients = new ArrayList<>();

    @Data
    public static class ClientConfig {
        private String clientId;
        private String clientSecret;
        private List<String> allowedScopes = new ArrayList<>();
        private String identityDomain;
    }
}

