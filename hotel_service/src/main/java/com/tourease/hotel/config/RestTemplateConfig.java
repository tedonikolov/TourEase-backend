package com.tourease.hotel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

@Configuration
public class RestTemplateConfig {
    @Value("${spring.rapid.api.key}")
    private String RAPID_API_KEY;
    @Bean
    @LoadBalanced
    public RestTemplate defaultRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate rapidApiRestTemplate(RestTemplateBuilder builder){
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler("https://booking-com15.p.rapidapi.com/api/v1/hotels/");
        return builder
                .uriTemplateHandler(uriTemplateHandler)
                .defaultHeader("X-RapidAPI-Key",RAPID_API_KEY)
                .defaultHeader("X-RapidAPI-Host","booking-com15.p.rapidapi.com")
                .build();
    }
}
