package com.nikitin.roadmaps;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@Theme(value = "roadmaps-frontend-vaadin")
public class RoadmapsFrontendVaadinApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapsFrontendVaadinApplication.class, args);
    }

}
