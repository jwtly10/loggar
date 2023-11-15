package com.jwtly10.loggar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.redis.RedisService;

import org.springframework.stereotype.Service;

@Service
public class LoggingServiceImpl implements LoggingService {

    private final RedisService redisPublisher;

    public LoggingServiceImpl(RedisService redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @Override
    public void log(String message, String level, String client) {
        try {
            LogEntry logEntry = new LogEntry(message, level, client);

            String serializedLogEntry = logEntry.toJson();

            redisPublisher.publish(serializedLogEntry);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
