package com.jwtly10.loggar.api;

import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.LoggingServiceImpl;
import com.jwtly10.loggar.service.security.SecurityService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LoggingController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    private final LoggingServiceImpl loggingService;

    private final SecurityService securityService;

    public LoggingController(LoggingServiceImpl loggingService, SecurityService securityService) {
        this.loggingService = loggingService;
        this.securityService = securityService;
    }

    @PostMapping("/log")
    public ResponseEntity<String> logMessage(
            @Valid @RequestBody LogEntry logEntry,
            @RequestHeader("Authorization") String apiKey,
            @RequestHeader("Timestamp") String timestamp,
            @RequestHeader("Client") String client) {

        try {
            if (securityService.validateKey(apiKey, timestamp, client)) {
                loggingService.log(
                        logEntry.getMessage(), logEntry.getLevel(), logEntry.getClient());
                return ResponseEntity.ok("Log entry created");
            } else {
                return ResponseEntity.status(401).body("Authorization failed");
            }
        } catch (Exception e) {
            logger.error("Error creating log entry", e);
            return ResponseEntity.badRequest().body("Error creating log entry");
        }
    }

    // TODO: Validate list of log entries
    @PostMapping("/logs")
    public ResponseEntity<String> logMessages(@Valid @RequestBody List<LogEntry> logEntry) {
        // try {
        // Integer count = 0;
        // for (LogEntry log : logEntry) {
        // loggingService.log(log.getMessage(), log.getLevel(), log.getClient());
        // count++;
        // }
        // return ResponseEntity.ok(count + " log entries created");
        // } catch (Exception e) {
        // logger.error("Error creating log entries", e);
        // return ResponseEntity.badRequest().body("Error creating log entries");
        // }
        //
        return ResponseEntity.ok("Not implemented");
    }
}
