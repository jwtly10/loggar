package com.jwtly10.loggar.integration.service.elasticcloud;

import com.jwtly10.loggar.model.LogEntry;
import com.jwtly10.loggar.service.elasticcloud.ElasticCloudService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ElasticCloudServiceTest {

    private final ElasticCloudService elasticCloudService;

    @Autowired
    public ElasticCloudServiceTest(ElasticCloudService elasticCloudService) {
        this.elasticCloudService = elasticCloudService;
    }

    @Test
    public void testElasticCloudIndexing() {
        LogEntry logEntry = new LogEntry("Unit_Test-testElasticCloudIndexing", "INFO", "Unit_Test");

        try {
            elasticCloudService.indexLogEntry(logEntry);
        } catch (Exception e) {
            // Fail the test if an exception is thrown
            e.printStackTrace();
            assert false;
        }
    }
}
