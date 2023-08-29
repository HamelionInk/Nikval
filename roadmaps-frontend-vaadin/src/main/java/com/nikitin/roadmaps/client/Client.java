package com.nikitin.roadmaps.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class Client {

    private final RestTemplate restTemplate;

    public ResponseEntity<String> request(String url, HttpMethod httpMethod, HttpEntity<?> request) {
        try {
            return restTemplate.exchange(url, httpMethod, request, String.class);
        } catch (HttpStatusCodeException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsString());
        }
    }

    public <T> HttpEntity<T> buildRequestBody(T requestDto) {
        return new HttpEntity<>(requestDto, buildMultiValueForHeader());
    }

    private MultiValueMap<String, String> buildMultiValueForHeader() {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", "Bearer " + getAccessToken());

        return header;
    }

    private String getAccessToken() {
        var userInfo = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userInfo.getIdToken().getTokenValue();
    }
}
