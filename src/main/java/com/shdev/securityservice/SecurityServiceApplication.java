package com.shdev.securityservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Security Service.
 */
@Slf4j
@SpringBootApplication
public class SecurityServiceApplication {

    /**
     * Main method to run the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        log.info("Starting Security Service Application");
        SpringApplication.run(SecurityServiceApplication.class, args);
        log.info("Security Service Application started successfully");
    }

}
