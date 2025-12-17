package com.shdev.securityservice.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * JWT token generator using RS256 algorithm.
 *
 * @author Shailesh Halor
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtProperties jwtProperties;
    private PrivateKey privateKey;

    /**
     * Generate JWT token with the specified claims.
     *
     * @param clientId the client ID (subject and client claim)
     * @param scope    the scope
     * @param domain   the identity domain name
     * @param roles    the user roles
     * @return JWT token string
     */
    public String generateToken(String clientId, String scope, String domain, List<String> roles) {
        log.debug("Generating JWT token for client: {}, scope: {}, domain: {}, roles: {}", clientId, scope, domain, roles);

        if (privateKey == null) {
            privateKey = loadPrivateKey();
        }

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getExpirationSeconds());

        String jti = generateJti();

        String token = Jwts.builder()
                .header()
                    .add("kid", "KeyPair_" + domain)
                    .add("x5t", generateThumbprint())
                    .and()
                .issuer(jwtProperties.getIssuer())
                .audience().add(jwtProperties.getAudience()).and()
                .expiration(Date.from(expiration))
                .id(jti)
                .issuedAt(Date.from(now))
                .subject(clientId)
                .claim("client", clientId)
                .claim("scope", List.of(scope))
                .claim("domain", domain)
                .claim("roles", roles)  // Add roles to JWT claims
                .claim("v", jwtProperties.getVersion())
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        log.debug("JWT token generated successfully with JTI: {} and roles: {}", jti, roles);
        return token;
    }

    /**
     * Load private key from file.
     *
     * @return PrivateKey instance
     */
    private PrivateKey loadPrivateKey() {
        try {
            log.info("Loading private key from: {}", jwtProperties.getPrivateKeyPath());
            String key = new String(Files.readAllBytes(Paths.get(jwtProperties.getPrivateKeyPath())));

            // Remove PEM headers/footers and whitespace
            key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            log.info("Private key loaded successfully");
            return keyFactory.generatePrivate(spec);
        } catch (IOException e) {
            log.error("Failed to read private key file: {}", jwtProperties.getPrivateKeyPath(), e);
            throw new RuntimeException("Failed to load private key", e);
        } catch (Exception e) {
            log.error("Failed to parse private key", e);
            throw new RuntimeException("Failed to parse private key", e);
        }
    }

    /**
     * Generate a unique JWT ID (JTI).
     *
     * @return JTI string
     */
    private String generateJti() {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(UUID.randomUUID().toString().getBytes())
                .substring(0, 22);
    }

    /**
     * Generate a thumbprint for the x5t header.
     *
     * @return thumbprint string
     */
    private String generateThumbprint() {
        // Simplified thumbprint generation - in production, this should be the actual certificate thumbprint
        return Base64.getEncoder().withoutPadding()
                .encodeToString(UUID.randomUUID().toString().getBytes())
                .substring(0, 20);
    }
}

