package com.tourease.gateway_service.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        String connectionProviderName = "tourease";
        int maxConnections = 400;
        HttpClient httpClient = HttpClient.create(ConnectionProvider.create(connectionProviderName, maxConnections));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
