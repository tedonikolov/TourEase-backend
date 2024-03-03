package com.tourease.hotel;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "Hotel Service", version = "0.0.1",
        description = """
                Documentation APIs v0.0.1. Hotel-service functionality:
                \n-a. Processes hotel information.
                \n-b. Provides information on available rooms, prices and services.
                \n-c. Manage reservations and confirmations."""))
public class HotelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelServiceApplication.class, args);
    }

}
