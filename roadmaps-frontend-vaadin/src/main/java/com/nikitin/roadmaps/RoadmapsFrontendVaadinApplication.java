package com.nikitin.roadmaps;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@Theme(value = "roadmaps-frontend-vaadin")
public class RoadmapsFrontendVaadinApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapsFrontendVaadinApplication.class, args);
    }

}
