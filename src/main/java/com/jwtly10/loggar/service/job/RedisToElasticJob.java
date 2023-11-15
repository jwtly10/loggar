package com.jwtly10.loggar.service.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.elasticcloud.ElasticCloudService;
import com.jwtly10.loggar.service.redis.RedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RedisToElasticJob {

    private static final Logger logger = LoggerFactory.getLogger(RedisToElasticJob.class);

    private final RedisService redisService;

    private final ElasticCloudService elasticCloudService;

    private final ObjectMapper objectMapper;

    public RedisToElasticJob(
            RedisService redisService,
            ElasticCloudService elasticCloudService,
            ObjectMapper objectMapper) {
        this.redisService = redisService;
        this.elasticCloudService = elasticCloudService;
        this.objectMapper = objectMapper;
    }

    public boolean run() {
        AtomicInteger count = new AtomicInteger(0);
        try {
            Long index = redisService.getQueueSize();
            if (index == 0) {
                logger.info("Redis queue is empty");
            }

            redisService
                    .getMessages(index)
                    .forEach(
                            message -> {
                                try {
                                    elasticCloudService.indexLogEntry(
                                            objectMapper.readValue(message, LogEntry.class));
                                    count.getAndIncrement();
                                } catch (Exception e) {
                                    logger.error("Error indexing log entry", e);
                                }
                            });

            if (count.get() != index) {
                logger.error(
                        "Redis queue size ({}) does not match number of indexed log entries ({})",
                        index,
                        count.get());
                return false;
            }

            logger.info("Successfully processed {} Redis messages out of {}", count.get(), index);
            return true;

        } catch (Exception e) {
            logger.error("Error running RedisToElasticJob", e);
            return false;
        }
    }
}
