package com.bruce.config;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.apache.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchClient {
    @Bean
    public static RestHighLevelClient createClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.200.130", 9200, "http"))); // Elasticsearch 地址
    }
}
