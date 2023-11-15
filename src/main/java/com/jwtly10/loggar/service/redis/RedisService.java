package com.jwtly10.loggar.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    @Value("${spring.redis.channel}")
    private String redisChannel;

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void publish(String message) {
        stringRedisTemplate.convertAndSend(redisChannel, message);

        // Push to list for job to pickup later
        stringRedisTemplate.opsForList().leftPush(redisChannel, message);
    }

    public Long getQueueSize() {
        return stringRedisTemplate.opsForList().size(redisChannel);
    }

    public List<String> getMessages(Long index) {
        return stringRedisTemplate.opsForList().range(redisChannel, 0, index - 1);
    }

    public void deleteMessages(Long index) {
        stringRedisTemplate.opsForList().trim(redisChannel, index, -1);
    }
}
