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

@Service
public class ElasticCloudService {

    @Value("${elastic.index.name}")
    private String elasticIndex;

    private static final Logger logger = LoggerFactory.getLogger(ElasticCloudService.class);

    private final ElasticsearchClient elasticsearchClient;

    private ElasticCloudService(
            @Value("${elastic.cloud.url}") String cloudUrl,
            @Value("${elastic.cloud.api.key}") String apiKey) {
        this.elasticsearchClient = createElasticsearchClient(cloudUrl, apiKey);
    }

    private ElasticsearchClient createElasticsearchClient(String cloudUrl, String apiKey) {
        RestClient restClient =
                RestClient.builder(HttpHost.create(cloudUrl))
                        .setDefaultHeaders(
                                new Header[] {new BasicHeader("Authorization", "ApiKey " + apiKey)})
                        .build();

        ElasticsearchTransport transport =
                new RestClientTransport(restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

    public void indexLogEntry(LogEntry logEntry) throws ElasticsearchException, IOException {

        IndexResponse res =
                elasticsearchClient.index(i -> i.index(elasticIndex).document(logEntry));

        logger.debug("Indexed log entry with ID: {}", res);
    }
}
