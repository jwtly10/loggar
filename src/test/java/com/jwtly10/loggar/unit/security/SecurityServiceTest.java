package com.jwtly10.loggar.unit.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jwtly10.loggar.service.security.SecurityService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class SecurityServiceTest {

    private final SecurityService securityService;

    @Value("${jwtly10.security.key}")
    private String internalKey;

    @Autowired
    public SecurityServiceTest(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Test
    public void testValidKey() {
        Long currentTimestamp = Instant.now().getEpochSecond();
        String req = "Unit_Test" + currentTimestamp + internalKey;

        try {
            String message = securityService.hash(req);
            assertEquals(
                    true,
                    securityService.validateKey(message, currentTimestamp.toString(), "Unit_Test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidClient() {
        Long currentTimestamp = Instant.now().getEpochSecond();
        String req = "Invalid Client" + currentTimestamp + internalKey;

        try {
            String message = securityService.hash(req);
            assertEquals(
                    false,
                    securityService.validateKey(
                            message, currentTimestamp.toString(), "Invalid Client"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentTimestamp = Instant.now().getEpochSecond();
        req = "Invalid Client" + currentTimestamp + internalKey;

        try {
            String message = securityService.hash(req);
            assertEquals(
                    false,
                    securityService.validateKey(message, currentTimestamp.toString(), "Unit_Test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidTimestamp() {
        Long invalidTimestamp = Instant.now().getEpochSecond();

        Instant instant = Instant.ofEpochSecond(invalidTimestamp);
        // Subtract 6 minutes from the current timestamp
        instant = instant.minusSeconds(500);

        invalidTimestamp = instant.getEpochSecond();

        String req = "Unit_Test" + invalidTimestamp + internalKey;

        try {
            String message = securityService.hash(req);
            assertEquals(
                    false,
                    securityService.validateKey(message, invalidTimestamp.toString(), "Unit_Test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
