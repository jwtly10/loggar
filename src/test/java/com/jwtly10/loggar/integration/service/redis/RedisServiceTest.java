package com.jwtly10.loggar.integration.service.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.redis.RedisService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

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

            assertEquals(Long.valueOf(1), redisService.getQueueSize());

            redisService.deleteMessages(redisService.getQueueSize());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRedisGetMessages() {

        try {
            LogEntry logEntry = new LogEntry("Unit_Test-testRedisGetMessages", "INFO", "Unit_Test");
            String serializedLogEntry = logEntry.toJson();
            redisService.publish(serializedLogEntry);

            Long index = redisService.getQueueSize();
            if (redisService.getMessages(index).isEmpty()) {
                throw new RuntimeException("Redis queue is empty");
            }

            List<String> messages = redisService.getMessages(index);
            assertEquals(messages.size(), redisService.getQueueSize());

            redisService.deleteMessages(redisService.getQueueSize());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
