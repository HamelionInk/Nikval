package com.nikitin.roadmaps.agent;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public abstract class Agent {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "http://localhost:8080";

    public Agent() {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(URL));
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
