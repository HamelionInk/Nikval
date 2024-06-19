package com.nikitin.roadmapfrontend.configuration.rest;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Getter
@Configuration
public class RestTemplateConfiguration {

    @Value("${backend.url}")
    private String url;

    @Bean("KeycloakRestTemplate")
    public RestTemplate keycloakRestTemplate() {
        var restTemplate = new RestTemplate();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(100000);

        var defaultUriBuilderFactory = new DefaultUriBuilderFactory(url);
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }
}
