package com.jwtly10.loggar.integration.service.job;

import com.jwtly10.loggar.service.job.RedisToElasticJob;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RedisToElasticJobTest {

    private final RedisToElasticJob redisToElasticJob;

    @Autowired
    public RedisToElasticJobTest(RedisToElasticJob redisToElasticJob) {
        this.redisToElasticJob = redisToElasticJob;
    }

    @Test
    public void testRedisToElasticJob() {
        if (redisToElasticJob.run()) {
            assert true;
        } else {
            assert false;
        }
    }
}
