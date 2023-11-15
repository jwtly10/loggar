package com.jwtly10.loggar.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.loggar.annotations.ValidLogLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.time.Instant;

@Data
public class LogEntry {

    public enum LogLevel {
        INFO,
        DEBUG,
        WARN,
        ERROR
    }

    @NotBlank(message = "Message cannot be blank or null")
    private String message;

    @NotNull(message = "Level cannot be null")
    @ValidLogLevel
    private String level;

    private String timestamp;

    @NotBlank(message = "Client cannot be blank or null")
    private String client;

    @JsonCreator
    public LogEntry(
            @JsonProperty("message") String message,
            @JsonProperty("level") String level,
            @JsonProperty("client") String client) {
        this.message = message;
        this.level = level;
        this.client = client;
        this.timestamp = Instant.ofEpochMilli(System.currentTimeMillis()).toString();
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
