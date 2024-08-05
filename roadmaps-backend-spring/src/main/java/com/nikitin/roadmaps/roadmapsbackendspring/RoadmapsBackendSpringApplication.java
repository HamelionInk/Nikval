package com.nikitin.roadmaps.roadmapsbackendspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@SpringBootApplication
public class RoadmapsBackendSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapsBackendSpringApplication.class, args);
    }
}
