package com.nikitin.roadmapfrontend;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "roadmap-frontend")
public class RoadmapFrontendApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(RoadmapFrontendApplication.class, args);
    }

}
