package com.tourease.configuration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

@Configuration
public class RestTemplateConfig {
    @Value("${spring.rapid.api.key}")
    private String RAPID_API_KEY;

    @Bean
    public RestTemplate rapidApiRestTemplate(RestTemplateBuilder builder){
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler("https://currency-conversion-and-exchange-rates.p.rapidapi.com/");
        return builder
                .uriTemplateHandler(uriTemplateHandler)
                .defaultHeader("X-RapidAPI-Key",RAPID_API_KEY)
                .defaultHeader("X-RapidAPI-Host","currency-conversion-and-exchange-rates.p.rapidapi.com")
                .build();
    }
}
