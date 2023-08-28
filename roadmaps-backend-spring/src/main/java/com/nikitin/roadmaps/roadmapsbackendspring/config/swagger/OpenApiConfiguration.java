package com.nikitin.roadmaps.roadmapsbackendspring.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8081/v1/api/roadmaps"))
                .info(new Info()
                        .title("Карта развития")
                        .version("0.0.1"));
    }
}
