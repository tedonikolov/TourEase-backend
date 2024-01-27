package com.tourease.logger.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userServiceTopic() {
        return TopicBuilder.name("user_service").build();
    }

    @Bean
    public NewTopic coreServiceTopic() {
        return TopicBuilder.name("core_service").build();
    }

    @Bean
    public NewTopic gatewayServiceTopic() {
        return TopicBuilder.name("gateway_service").build();
    }

    @Bean
    public NewTopic hotelServiceTopic() {
        return TopicBuilder.name("hotel_service").build();
    }

    @Bean
    public NewTopic transportServiceTopic() {
        return TopicBuilder.name("transport_service").build();
    }

    @Bean
    public NewTopic configurationServiceTopic() {
        return TopicBuilder.name("configuration-service").build();
    }
}