package com.tourease.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "User Service", version = "0.0.1",
        description = """
                Documentation APIs v0.0.1. User-service functionality:
                \n-a. Responsible for managing user profiles.
                \n-b. User registration and login.
                \n-c. Manage personal data and account settings."""))
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
