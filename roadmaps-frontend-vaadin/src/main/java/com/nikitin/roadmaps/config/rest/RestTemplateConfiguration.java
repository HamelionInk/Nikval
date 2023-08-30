package com.nikitin.roadmaps.config.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateConfiguration {

    private final String url = "http://localhost:8081/api/v1/roadmaps";

    @Bean
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(100000);

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(url));
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}