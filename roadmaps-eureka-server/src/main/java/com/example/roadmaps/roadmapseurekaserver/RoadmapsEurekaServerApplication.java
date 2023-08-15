package com.example.roadmaps.roadmapseurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RoadmapsEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapsEurekaServerApplication.class, args);
    }

}
