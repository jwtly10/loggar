package com.jwtly10.loggar.service;

import com.jwtly10.loggar.redis.RedisPublisher;

import org.springframework.stereotype.Service;

@Service
public class LoggingServiceImpl implements LoggingService {

    private final RedisPublisher redisPublisher;

    public LoggingServiceImpl(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @Override
    public void log(String message, String level) {
        redisPublisher.publishToChannel(message);
    }
}
