package com.nikitin.roadmaps.roadmapsconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class RoadmapsConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapsConfigServerApplication.class, args);
    }

}
