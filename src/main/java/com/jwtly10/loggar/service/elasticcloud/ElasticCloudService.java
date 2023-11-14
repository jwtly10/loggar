package com.jwtly10.loggar.service.elasticcloud;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import com.jwtly10.loggar.model.LogEntry;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

// TODO: Refactor to inject dependencies

@Service
public class ElasticCloudService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticCloudService.class);

    @Value("${elastic.cloud.id}")
    private String cloudId;

    @Value("${elastic.cloud.api.key}")
    private String apiKey;

    @Value("${elastic.cloud.url}")
    private String cloudUrl;

    private ElasticsearchClient elasticsearchClient;

    public void indexLogEntry(LogEntry logEntry) throws ElasticsearchException, IOException {

        // TODO: Refactor this, this is a hack to get round EC side timestamp parsing
        logEntry.setTimestamp(
                Instant.ofEpochMilli(Long.parseLong(logEntry.getTimestamp())).toString());

        RestClient restClient =
                RestClient.builder(HttpHost.create(cloudUrl))
                        .setDefaultHeaders(
                                new Header[] {new BasicHeader("Authorization", "ApiKey " + apiKey)})
                        .build();

        ElasticsearchTransport transport =
                new RestClientTransport(restClient, new JacksonJsonpMapper());

        elasticsearchClient = new ElasticsearchClient(transport);
        ElasticsearchClient esClient = elasticsearchClient;

        IndexResponse res = esClient.index(i -> i.index("loggar").document(logEntry));

        logger.info("Indexed log entry with ID: {}", res);
    }
}
