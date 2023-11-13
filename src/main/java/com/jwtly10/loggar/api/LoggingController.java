package com.jwtly10.loggar.api;

import com.jwtly10.loggar.service.LoggingServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoggingController {

    private LoggingServiceImpl loggingService;

    public LoggingController(LoggingServiceImpl loggingService) {
        this.loggingService = loggingService;
    }

    @PostMapping("/log")
    public String log() {
        loggingService.log("Hello World!", "INFO");
        return "Message sent to Redis";
    }
}
