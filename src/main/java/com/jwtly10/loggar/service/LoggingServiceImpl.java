package com.jwtly10.loggar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.redis.RedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingServiceImpl implements LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);

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

            logger.info("Log entry created for: {}", client);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
