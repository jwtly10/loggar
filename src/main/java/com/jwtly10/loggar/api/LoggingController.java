package com.jwtly10.loggar.api;

import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.LoggingServiceImpl;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoggingController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    private final LoggingServiceImpl loggingService;

    public LoggingController(LoggingServiceImpl loggingService) {
        this.loggingService = loggingService;
    }

    @PostMapping("/log")
    public ResponseEntity<String> logMessage(@Valid @RequestBody LogEntry logEntry) {
        try {
            loggingService.log(logEntry.getMessage(), logEntry.getLevel(), logEntry.getClient());
            return ResponseEntity.ok("Log entry created");
        } catch (Exception e) {
            logger.error("Error creating log entry", e);
            return ResponseEntity.badRequest().body("Error creating log entry");
        }
    }
}
