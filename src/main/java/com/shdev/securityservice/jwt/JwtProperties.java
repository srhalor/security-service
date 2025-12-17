package com.shdev.securityservice.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for JWT token generation and validation.
 *
 * @author Shailesh Halor
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * JWT issuer URL (e.g., "https://<base-url>/oauth2")
     */
    private String issuer;

    /**
     * JWT audience list
     */
    private List<String> audience;

    /**
     * Token expiration time in seconds (default: 3600 = 1 hour)
     */
    private long expirationSeconds = 3600;

    /**
     * Path to RSA private key file for signing tokens
     */
    private String privateKeyPath;

    /**
     * Path to RSA public key file for validating tokens
     */
    private String publicKeyPath;

    /**
     * JWT version (default: "1.0")
     */
    private String version = "1.0";

    /**
     * Identity domain name for OAuth2
     */
    private String identityDomainName;
}

