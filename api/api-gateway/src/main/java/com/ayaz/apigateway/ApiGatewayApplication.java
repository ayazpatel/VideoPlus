package com.ayaz.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the API Gateway.
 * @EnableDiscoveryClient enables the application to register with a discovery server like Eureka.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        System.out.println("<<<<< API GATEWAY IS STARTING UP, CHECK FOR THIS MESSAGE! >>>>>");
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}