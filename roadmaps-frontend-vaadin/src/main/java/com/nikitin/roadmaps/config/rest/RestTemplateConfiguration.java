package com.nikitin.roadmaps.config.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateConfiguration {

    //todo - перенести в application
    private final String url = "https://api.roadmaps-nikval.ru/api/v1/roadmaps";

    @Bean("KeycloakRestTemplate")
    public RestTemplate keycloakRestTemplate() {
        var restTemplate = new RestTemplate();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(100000);

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(url));
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
