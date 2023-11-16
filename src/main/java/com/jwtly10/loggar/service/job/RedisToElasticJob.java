package com.jwtly10.loggar.service.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.loggar.exceptions.JobException;
import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.elasticcloud.ElasticCloudService;
import com.jwtly10.loggar.service.redis.RedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisToElasticJob {

    private static final Logger logger = LoggerFactory.getLogger(RedisToElasticJob.class);

    private RedisService redisService;

    private ElasticCloudService elasticCloudService;

    private ObjectMapper objectMapper;

    public RedisToElasticJob(
            RedisService redisService,
            ElasticCloudService elasticCloudService,
            ObjectMapper objectMapper) {
        this.redisService = redisService;
        this.elasticCloudService = elasticCloudService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${redis-to-elastic-job.fixed-delay}")
    public void run() {
        logger.info("Running RedisToElasticJob");

        try {
            Long index = redisService.getQueueSize();
            if (index == 0) {
                logger.info("Redis queue is empty");
                return;
            }

            List<String> messages = redisService.getMessages(index);

            for (String message : messages) {
                processMessage(message);
            }

            redisService.deleteMessages(index);

            logger.info("Redis queue successfully processed & cleared");

        } catch (Exception e) {
            logger.error("Error running RedisToElasticJob", e);
            throw new JobException("Error running RedisToElasticJob");
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void processMessage(String message) {
        logger.info("Processing message");
        try {
            elasticCloudService.indexLogEntry(objectMapper.readValue(message, LogEntry.class));
        } catch (Exception e) {
            logger.error("Error indexing log entry", e);
            throw new RuntimeException(e);
        }
    }
}
