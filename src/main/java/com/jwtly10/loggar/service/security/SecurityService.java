package com.jwtly10.loggar.service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SecurityService {

    @Value("${jwtly10.security.key}")
    private String internalKey;

    @Value("${jwtly10.security.clients}")
    private String internalClients;

    public boolean validateKey(String apiKey, String timestamp, String client) {
        if (!validTimestamp(timestamp)) {
            return false;
        }

        if (!validClient(client)) {
            return false;
        }

        String validKey = generateKey(client);
        if (validKey == null) {
            return false;
        }

        return apiKey.equals(validKey);
    }

    private String generateKey(String client) {
        Long currentTimestamp = Instant.now().getEpochSecond();
        String message = client + currentTimestamp + internalKey;
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
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));

        return new String(hash, StandardCharsets.UTF_8);
    }
}
