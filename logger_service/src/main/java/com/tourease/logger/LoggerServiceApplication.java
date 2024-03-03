package com.tourease.logger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "Logger Service", version = "0.0.1",
        description = """
                Documentation APIs v0.0.1. Logger-service functionality:
                \n-a. It is responsible for logging events and generating logs.
                \n-b. Helps track system activity and troubleshoot issues."""))
public class LoggerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggerServiceApplication.class, args);
    }

}
