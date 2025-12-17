package com.shdev.securityservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Test to generate BCrypt password hashes for OAuth2 client secrets.
 * <p>
 * This test runs ONLY when explicitly enabled with -DgenerateHash=true.
 * This prevents it from running during normal test execution.
 * </p>
 * <p>
 * Usage:
 * 1. To generate hash for default password:
 *    mvn test -Dtest=PasswordHashGeneratorTest -DgenerateHash=true
 * 2. To generate hash for YOUR password:
 *    - Edit the plainPassword variable below
 *    - Run: mvn test -Dtest=PasswordHashGeneratorTest -DgenerateHash=true
 *    - Copy the generated hash to application.yml
 * </p>
 */
@Slf4j
public class PasswordHashGeneratorTest {

    @Test
    @EnabledIfSystemProperty(named = "generateHash", matches = "true",
                            disabledReason = "This test generates BCrypt hashes and should only run manually with -DgenerateHash=true")
    void generatePasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // ============================================
        // CHANGE THIS to generate hash for YOUR password
        // ============================================
        String plainPassword = "jgak824fHdK39gs8ga";

        String hashedPassword = encoder.encode(plainPassword);

        log.info("=================================================================================");
        log.info("BCrypt Password Hash Generator");
        log.info("=================================================================================");
        log.info("Plain text password: {}", plainPassword);
        log.info("BCrypt hash        : {}", hashedPassword);
        log.info("=================================================================================");

        // Verify the hash works
        boolean matches = encoder.matches(plainPassword, hashedPassword);
        log.info("Verification: {}", matches ? "✓ PASS" : "✗ FAIL");
        log.info("Hash length: {} characters (60 = valid BCrypt)", hashedPassword.length());
        log.info("=================================================================================");

    }
}

