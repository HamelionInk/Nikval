package com.nikitin.roadmapfrontend.configuration.rest;

import com.nikitin.roadmapfrontend.configuration.security.KeycloakTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateServiceImpl implements RestTemplateService {

    private final RestTemplate restTemplate;
    private final KeycloakTokenService keycloakTokenService;

    @Override
    public <T> T request(String url, HttpMethod httpMethod, HttpEntity<?> request, Map<String, ?> uriParams, Class<T> classType) {
        return restTemplate.exchange(url, httpMethod, request, classType, uriParams).getBody();
    }

    @Override
    public <T> HttpEntity<T> buildRequestBody(T requestDto, MultiValueMap<String, String> customHeader) {
        return new HttpEntity<>(requestDto, buildMultiValueForHeader(customHeader));
    }

    @Override
    public MultiValueMap<String, String> buildMultiValueForHeader(MultiValueMap<String, String> customHeader) {
        if(Objects.isNull(customHeader)) {
            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("Authorization", "Bearer " + keycloakTokenService.getAccessToken());

            return header;
        }
        customHeader.add("Authorization", "Bearer " + keycloakTokenService.getAccessToken());

        return customHeader;
    }

}
