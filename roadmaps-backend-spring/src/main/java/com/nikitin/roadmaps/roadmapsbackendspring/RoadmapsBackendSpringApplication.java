package com.nikitin.roadmaps.roadmapsbackendspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class RoadmapsBackendSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapsBackendSpringApplication.class, args);
    }

}
