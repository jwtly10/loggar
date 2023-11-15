package com.jwtly10.loggar.integration.service.redis;

import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.redis.RedisService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RedisServiceTest {

    private final RedisService redisService;

    @Autowired
    public RedisServiceTest(RedisService redisService) {
        this.redisService = redisService;
    }

    @Test
    public void testRedisPublish() {

        try {

            LogEntry logEntry = new LogEntry("Unit_Test-testRedisPublish", "INFO", "Unit_Test");
            String serializedLogEntry = logEntry.toJson();
            redisService.publish(serializedLogEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRedisGetMessages() {
        Long index = redisService.getQueueSize();
        if (redisService.getMessages(index).isEmpty()) {
            throw new RuntimeException("Redis queue is empty");
        }
    }
}
