package com.jwtly10.loggar.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    @Value("${spring.redis.channel}")
    private String redisChannel;

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisPublisher(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void publishToChannel(String message) {
        stringRedisTemplate.convertAndSend(redisChannel, message);
    }
}
