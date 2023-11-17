package com.jwtly10.loggar.integration.service.job;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.elasticcloud.ElasticCloudService;
import com.jwtly10.loggar.service.job.RedisToElasticJob;
import com.jwtly10.loggar.service.redis.RedisService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RedisToElasticJobTest {

    @Mock private RedisService redisService;

    private RedisToElasticJob redisToElasticJob;

    @Autowired private ElasticCloudService elasticCloudService;

    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        redisToElasticJob = new RedisToElasticJob(redisService, elasticCloudService, objectMapper);
    }

    @Test
    public void testRedisToElasticJob() {
        // Mock redis list in this case, as we test redis seperately
        LogEntry mockedLog1 = new LogEntry("MockedLog1", "INFO", "Unit_Test");
        LogEntry mockedLog2 = new LogEntry("MockedLog2", "INFO", "Unit_Test");
        LogEntry mockedLog3 = new LogEntry("MockedLog3", "INFO", "Unit_Test");

        List<String> mockedRedisMessages = null;

        try {
            mockedRedisMessages =
                    List.of(mockedLog1.toJson(), mockedLog2.toJson(), mockedLog3.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(redisService.getQueueSize()).thenReturn(Long.valueOf(mockedRedisMessages.size()));
        when(redisService.getMessages(3L)).thenReturn(mockedRedisMessages);

        try {
            this.redisToElasticJob.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(redisService, times(1)).getQueueSize();
        verify(redisService, times(1)).getMessages(Long.valueOf(mockedRedisMessages.size()));
        verify(redisService, times(1)).deleteMessages(Long.valueOf(mockedRedisMessages.size()));

        // ElasticSearch is configured for testing so not mocked
    }
}
