package com.jwtly10.loggar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.redis.RedisPublisher;

import org.springframework.stereotype.Service;

@Service
public class LoggingServiceImpl implements LoggingService {

    private final RedisPublisher redisPublisher;

    public LoggingServiceImpl(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @Override
    public void log(String message, String level, String client) {
        try {
            LogEntry logEntry = new LogEntry(message, level, client);

            String serializedLogEntry = logEntry.toJson();

            redisPublisher.publishToChannel(serializedLogEntry);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
