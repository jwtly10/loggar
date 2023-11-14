package com.jwtly10.loggar.service.elasticcloud;

import com.jwtly10.loggar.model.LogEntry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElasticCloudServiceTest {

    private final ElasticCloudService elasticCloudService;

    @Autowired
    public ElasticCloudServiceTest(ElasticCloudService elasticCloudService) {
        this.elasticCloudService = elasticCloudService;
    }

    @Test
    public void testElasticCloudIndex() {
        LogEntry logEntry = new LogEntry("Testing if EC tests are working", "INFO", "FileFlow");

        try {
            elasticCloudService.indexLogEntry(logEntry);
        } catch (Exception e) {
            // Fail the test if an exception is thrown
            e.printStackTrace();
            assert false;
        }
    }
}
