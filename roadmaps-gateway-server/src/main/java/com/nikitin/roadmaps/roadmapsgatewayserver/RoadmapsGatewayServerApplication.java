package com.nikitin.roadmaps.roadmapsgatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RoadmapsGatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapsGatewayServerApplication.class, args);
    }

}
