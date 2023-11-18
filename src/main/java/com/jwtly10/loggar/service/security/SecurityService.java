package com.jwtly10.loggar.service.security;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Formatter;

@Service
public class SecurityService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityService.class);

    @Value("${jwtly10.security.key}")
    private String internalKey;

    @Value("${jwtly10.security.clients}")
    private String internalClients;

    public boolean validateKey(String apiKey, String timestamp, String client) {
        if (!validTimestamp(timestamp)) {
            logger.error("Invalid timestamp: " + timestamp);
            return false;
        }

        if (!validClient(client)) {
            logger.error("Invalid client: " + client);
            return false;
        }

        String validKey = generateKey(client, timestamp);
        if (validKey == null) {
            return false;
        }

        return apiKey.equals(validKey);
    }

    private String generateKey(String client, String timestamp) {
        String message = client + timestamp + internalKey;
        try {
            return hash(message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean validTimestamp(String epoch) {
        long difference =
                ChronoUnit.MINUTES.between(
                        Instant.now(), Instant.ofEpochSecond(Long.parseLong(epoch)));
        return difference < 5;
    }

    private boolean validClient(String client) {
        String[] clients = internalClients.split(",");
        for (String c : clients) {
            if (c.equals(client)) {
                return true;
            }
        }
        return false;
    }

    public String hash(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        return byteArrayToHexString(hash);
    }

    private String byteArrayToHexString(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }
}
