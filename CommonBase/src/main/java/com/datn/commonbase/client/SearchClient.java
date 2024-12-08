package com.datn.commonbase.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class SearchClient {
    private static final Logger _log = LogManager.getLogger(SearchClient.class);
    private final ElasticsearchClient client;
    private final int maxTries;
    private final long sleep;
    private static SearchClient _instance;

    public SearchClient(ElasticsearchClient client, int maxTries, long sleep) {
        this.client = client;
        this.maxTries = maxTries;
        this.sleep = sleep;
    }

    public static SearchClient getInstance() {
        if (_instance == null) {
            _instance = new SearchClient(getElasticsearchClient(), 5, 400);
        }
        return _instance;
    }

    private static ElasticsearchClient getElasticsearchClient() {
        RestClient restClient = RestClient.builder(new HttpHost("search.tieuhocdong.online", 80))
                .setRequestConfigCallback(
                        requestConfigBuilder -> requestConfigBuilder
                                .setConnectTimeout(5000)
                                .setSocketTimeout(60000)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    public <Resp> Resp executeWithClient(Function<Resp, ElasticsearchClient> function) throws IOException {
        int count = 0;
        while (true) {
            try {
                return function.apply(this.client);
            } catch (IOException ie) {
                if (++count > maxTries) {
                    throw ie;
                }
                _log.error(String.format("Tries failed: %s/%s", count, maxTries));
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ignored) {
                }
            } catch (Exception e) {
                _log.error(e);
                return null;
            }
        }
    }
}
